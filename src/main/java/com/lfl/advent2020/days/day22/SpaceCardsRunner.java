package com.lfl.advent2020.days.day22;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

@Slf4j
@Service
public class SpaceCardsRunner implements LinesConsumer {

    private Player player1;
    private Player player2;
    private int gameNbrMax = 0;

    @Override
    public void consume(List<String> lines) {
        extractPlayers(lines);

        Player winner = runGame(player1, player2);

        log.info("Result = {}", winner.computeValue());
    }

    private Player runGame(Player player1, Player player2) {
        gameNbrMax++;
        int gameNbr = gameNbrMax;
        return doRunGame(player1, player2, gameNbr);
    }

    private Player doRunGame(Player player1, Player player2, int gameNbr) {
        int round = 1;

        Set<String> playedRounds = Sets.mutable.empty();
        while (player1.hasStillCards() && player2.hasStillCards()) {
            //            log.info("-- Round {} (Game {}) --", round, gameNbr);
            //            log.info("{}", player1);
            //            log.info("{}", player2);

            if (hadSameRound(player1, player2, playedRounds)) {
                //log.info("The winner of game {} is {}", gameNbr, player1.getName());
                return player1;
            }
            playedRounds.add(computeRound(player1, player2));
            playRound(player1, player2);

            //            log.info("{} wins round {} of game {}!", winner.getName(), round, gameNbr);
            //            log.info("");
            round++;
        }

        //        log.info("== Post-game results ==");
        //        log.info("{}", player1);
        //        log.info("{}", player2);

        Player winner = player1.hasStillCards() ? player1 : player2;

        //log.info("The winner of game {} is {}", gameNbr, winner.getName());

        return winner;
    }

    private void playRound(Player player1, Player player2) {
        int card1 = player1.play();
        int card2 = player2.play();

        Player winner = determineWinner(player1, player2, card1, card2);

        if (isSamePlayer(player1, winner)) {
            winner.getDeck().add(card1);
            winner.getDeck().add(card2);
        } else {
            winner.getDeck().add(card2);
            winner.getDeck().add(card1);
        }
    }

    private Player determineWinner(Player player1, Player player2, int card1, int card2) {
        if (shouldRunSubGame(player1, player2, card1, card2)) {
            Player winnerOfSubGame = runGame(player1.copy(card1), player2.copy(card2));
            return isSamePlayer(player1, winnerOfSubGame) ? player1 : player2;
        } else {
            //part 1
            return card1 > card2 ? player1 : player2;
        }
    }

    private boolean shouldRunSubGame(Player player1, Player player2, int card1, int card2) {
        return player1.getDeck().size() >= card1 && player2.getDeck().size() >= card2;
    }

    private boolean isSamePlayer(Player player1, Player winnerOfSubGame) {
        return player1.getName().equals(winnerOfSubGame.getName());
    }

    private boolean hadSameRound(Player player1, Player player2, Set<String> playedRounds) {
        return playedRounds.contains(computeRound(player1, player2));
    }

    private String computeRound(Player player1, Player player2) {
        return player1.toString() + "|" + player2.toString();
    }

    private void extractPlayers(List<String> lines) {
        MutableSet<Player> players = Sets.mutable.empty();
        List<String> acc = Lists.mutable.empty();
        for (String line : lines) {
            if (line.isEmpty()) {
                players.add(Player.of(acc));
                acc.clear();
                continue;
            }
            acc.add(line);
        }
        if (!acc.isEmpty()) {
            players.add(Player.of(acc));
        }

        player1 = players.detect(player -> player.getName().equals("Player 1"));
        player2 = players.detect(player -> player.getName().equals("Player 2"));
    }

    @Getter
    public static class Player {
        private String name;
        private MutableIntList deck;

        public static Player of(List<String> lines) {
            String name = lines.get(0).replace(":", "");
            MutableIntList deck = IntStream.range(1, lines.size())
                                           .mapToObj(lines::get)
                                           .mapToInt(Integer::parseInt)
                                           .collect(IntLists.mutable::empty,
                                                    MutableIntCollection::add,
                                                    MutableIntCollection::addAll);
            Player player = new Player();
            player.name = name;
            player.deck = deck;
            return player;
        }

        public int play() {
            int card = deck.get(0);
            //log.info("{} plays: {}", name, card);
            deck.remove(card);
            return card;
        }

        public BigInteger computeValue() {
            return IntStream.range(1, deck.size() + 1)
                            .map(i -> i * deck.get(deck.size() - i))
                            .mapToObj(BigInteger::valueOf)
                            .reduce(BigInteger::add)
                            .orElse(BigInteger.ZERO);
        }

        public boolean hasStillCards() {
            return deck.size() > 0;
        }

        public Player copy(int limit) {
            Player player = new Player();
            player.name = this.name;
            player.deck = this.deck.primitiveStream()
                                   .limit(limit)
                                   .collect(IntLists.mutable::empty,
                                            MutableIntCollection::add,
                                            MutableIntCollection::addAll);
            return player;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Player player = (Player) o;
            return name.equals(player.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return name + "'s deck:" + deck;
        }
    }

}
