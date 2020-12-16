package com.lfl.advent2020.days.day16;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.eclipse.collections.impl.tuple.Tuples;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
@Service
public class TicketValidator implements LinesConsumer {

    private final static Pattern rulePattern = Pattern.compile("(?<name>\\w+(\\s\\w+)?): (?<lower1>\\d+)-(?<upper1>\\d+) or (?<lower2>\\d+)-(?<upper2>\\d+)");
    private MutableList<MutableList<Integer>> nearbyTickets;
    private MutableList<Integer> myTicket;
    private MutableList<MutableList<Integer>> validTickets;
    @Getter
    private MutableSet<Rule> rules;
    @Setter
    private boolean doPart2 = true;
    @Getter
    private int ticketErrorRate;
    @Getter
    private BigInteger result;

    @Override
    public void consume(List<String> lines) {
        buildInput(lines);
        findTicketErrorRate();

        if (!doPart2) {
            return;
        }

        findValidTickets();
        findFields();

        result = rules.stream()
                      .filter(rule -> rule.getName().startsWith("departure"))
                      .map(Rule::getFieldNumber)
                      .map(index -> myTicket.get(index))
                      .map(BigInteger::valueOf)
                      .reduce(BigInteger::multiply)
                      .orElse(BigInteger.ZERO);

        log.info("Result = {}", result);
    }

    private void findFields() {
        int numberOfFields = rules.size();
        Set<Integer> fieldNumbersFound = Sets.mutable.withInitialCapacity(numberOfFields);

        while (rules.anySatisfy(rule -> rule.getFieldNumber() == -1)) {
            IntStream.range(0, numberOfFields)
                     .filter(fieldNumber -> !fieldNumbersFound.contains(fieldNumber))
                     .mapToObj(fieldNumber -> Tuples.pair(fieldNumber, findEligibleRules(validTickets, fieldNumber)))
                     .filter(pair -> pair.getTwo().size() == 1)
                     .peek(pair -> pair.getTwo().get(0).setFieldNumber(pair.getOne()))
                     .forEach(pair -> fieldNumbersFound.add(pair.getOne()));
        }
    }

    private void findValidTickets() {
        validTickets = nearbyTickets.select(ticket -> ticket.noneSatisfy(i -> rules.noneSatisfy(rule -> rule.isValid(i))));
    }

    private MutableList<Rule> findEligibleRules(MutableList<MutableList<Integer>> validTickets, int fieldNumber) {
        MutableList<Integer> fieldValues = validTickets.collect(ticket -> ticket.get(fieldNumber));
        return rules.stream()
                    .filter(rule -> rule.getFieldNumber() == -1)
                    .filter(rule -> fieldValues.allSatisfy(rule::isValid))
                    .collect(Collectors2.toList());
    }

    private void findTicketErrorRate() {
        ticketErrorRate = nearbyTickets.stream()
                                       .flatMap(List::stream)
                                       .filter(i -> rules.noneSatisfy(rule -> rule.isValid(i)))
                                       .mapToInt(i -> i)
                                       .sum();

        log.info("ticketErrorRate = {}", ticketErrorRate);
    }

    private void buildInput(List<String> lines) {
        int index = 0;
        rules = Sets.mutable.empty();
        while (!lines.get(index).isEmpty()) {
            rules.add(Rule.of(lines.get(index)));
            index++;
        }

        index++;
        index++;
        myTicket = Arrays.stream(lines.get(index).split(","))
                         .map(Integer::parseInt)
                         .collect(Collectors2.toList());

        index++;
        index++;
        index++;
        nearbyTickets = Lists.mutable.empty();
        for (int i = index; i < lines.size(); i++) {
            MutableList<Integer> ticket = Arrays.stream(lines.get(i).split(","))
                                                .map(Integer::parseInt)
                                                .collect(Collectors2.toList());

            nearbyTickets.add(ticket);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Rule {
        private String name;
        private int fieldNumber = -1;
        private ImmutableSet<IntInterval> intervals;

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public static Rule of(String line) {
            Matcher matcher = rulePattern.matcher(line);
            matcher.find();

            Rule rule = new Rule();
            rule.name = matcher.group("name");
            IntInterval interval1 = IntInterval.fromTo(Integer.parseInt(matcher.group("lower1")),
                                                       Integer.parseInt(matcher.group("upper1")));
            IntInterval interval2 = IntInterval.fromTo(Integer.parseInt(matcher.group("lower2")),
                                                       Integer.parseInt(matcher.group("upper2")));

            rule.intervals = Sets.immutable.of(interval1, interval2);
            return rule;
        }

        public boolean isValid(int number) {
            return intervals.anySatisfy(interval -> interval.contains(number));
        }
    }
}
