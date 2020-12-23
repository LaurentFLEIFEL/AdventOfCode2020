package com.lfl.advent2020.days.day23;

import com.lfl.advent2020.LinesConsumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
public class CrabCupRunner implements LinesConsumer {


    @Setter
    private int maxMoveNbr = 10_000_000;

    @Setter
    private boolean isPart2 = true;

    @Setter
    private boolean debug = false;

    @Setter
    private int nbrMax = 1_000_000;
    private final MutableIntObjectMap<Cup> cups = IntObjectMaps.mutable.empty();

    @Override
    public void consume(List<String> lines) {
        Cup current = parseAndReturnFirst(lines);
        for (int moveNbr = 0; moveNbr < maxMoveNbr; moveNbr++) {
            current = runMove(current, moveNbr);
        }

        Cup one = cups.get(1);
        if (isPart2) {
            int onePlusOneValue = one.next.value;
            int onePlusTwoValue = one.next.next.value;

            BigInteger result = BigInteger.valueOf(onePlusOneValue).multiply(BigInteger.valueOf(onePlusTwoValue));
            log.info("result = '{}'", result);
        } else {
            current = one.next;
            String result = "";
            while (current.value != 1) {
                result += current.value;
                current = current.next;
            }
            log.info("result = '{}'", result);
        }
    }

    private Cup runMove(Cup current, int moveNbr) {
        if (debug) {
            log.info("-- move {} --", moveNbr + 1);
            printCupList(current);
        }

        Cup firstMoving = current.next;
        Cup lastMoving = current.next.next.next;


        int destinationValue = findDestinationValue(current, firstMoving, lastMoving);
        if (debug) {
            log.info("pick up: {}, {}, {}", firstMoving.value, firstMoving.next.value, lastMoving.value);
            log.info("destination: {}", destinationValue);
            log.info("");
        }
        Cup destination = cups.get(destinationValue);

        current.next = lastMoving.next;
        lastMoving.next = destination.next;
        destination.next = firstMoving;

        current = current.next;
        return current;
    }

    private int findDestinationValue(Cup current, Cup firstMoving, Cup lastMoving) {
        int destinationValue = current.value == 1 ? nbrMax : current.value - 1;
        while (destinationValue == firstMoving.value
               || destinationValue == firstMoving.next.value
               || destinationValue == lastMoving.value) {
            destinationValue = destinationValue == 1 ? nbrMax : destinationValue - 1;
        }
        return destinationValue;
    }

    private Cup parseAndReturnFirst(List<String> lines) {
        String[] split = lines.get(0).split("");
        Cup current;
        Cup first = null;
        Cup previous = null;
        for (int index = 0; index < split.length; index++) {
            int value = Integer.parseInt(split[index]);
            current = Cup.of(value);
            cups.put(value, current);
            if (index == 0) {
                first = current;
                previous = first;
                continue;
            }

            previous.next = current;
            previous = current;
        }

        if (isPart2) {
            int nbr = 10;
            while (nbr <= nbrMax) {
                current = Cup.of(nbr);
                cups.put(nbr, current);
                previous.next = current;
                previous = current;
                nbr++;
            }
        }

        previous.next = first;
        return first;
    }

    private void printCupList(Cup first) {
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(first.value).append(')');
        Cup cup = first.next;
        while (cup != first) {
            sb.append(", ").append(cup.value);
            cup = cup.next;
        }
        log.info("cups: {}", sb.toString());
    }

    private static class Cup {
        public int value;
        public Cup next;

        public static Cup of(int value) {
            Cup cup = new Cup();
            cup.value = value;
            return cup;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
}
