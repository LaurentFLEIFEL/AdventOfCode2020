package com.lfl.advent2020.days.day18;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Lists;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Slf4j
@Service
public class MathEvaluator implements LinesConsumer {

    @Getter
    private BigInteger sum1;
    @Getter
    private BigInteger sum2;

    @Override
    public void consume(List<String> lines) {
        sum1 = lines.stream()
                    .map(line -> line.replaceAll(" ", ""))
                    .map(this::evaluate1)
                    .reduce(BigInteger::add)
                    .orElse(BigInteger.ZERO);

        log.info("sum1 = {}", sum1);

        sum2 = lines.stream()
                    .map(line -> line.replaceAll(" ", ""))
                    .map(this::evaluate2)
                    .reduce(BigInteger::add)
                    .orElse(BigInteger.ZERO);

        log.info("sum2 = {}", sum2);
    }

    public BigInteger evaluate2(String line) {
        return evaluate(line, true);
    }

    public BigInteger evaluate1(String line) {
        return evaluate(line, false);
    }

    public BigInteger evaluate(String line, boolean isPart2) {
        List<String> npi = toNpi(line, isPart2);
        Deque<String> stack = new ArrayDeque<>();

        for (String s : npi) {
            if ("+".equals(s) || "*".equals(s)) {
                String a = stack.pollLast();
                String b = stack.pollLast();
                BigInteger evaluate = Operator.of(s)
                                              .evaluate(new BigInteger(a), new BigInteger(b));

                stack.add(evaluate.toString());
            } else {
                stack.add(s);
            }
        }

        return new BigInteger(stack.pollLast());
    }

    private List<String> toNpi(String line, boolean isPart2) {
        List<String> npi = Lists.mutable.empty();
        Deque<String> transitionStack = new ArrayDeque<>();
        StringBuilder numberBuilder = new StringBuilder();
        for (int index = 0; index < line.length(); index++) {
            char c = line.charAt(index);
            if (Character.isDigit(c)) {
                numberBuilder.append(c);
                continue;
            } else {
                if (!"".equals(numberBuilder.toString())) {
                    String numberS = numberBuilder.toString();
                    numberBuilder = new StringBuilder();
                    npi.add(numberS);
                }
            }

            switch (c) {
                case '+':
                    if (!isPart2) {
                        while ("*".equals(transitionStack.peekLast())) {//* has equal or more priority
                            npi.add("*");
                            transitionStack.removeLast();
                        }
                    }
                    transitionStack.add("+");
                    break;
                case '*':
                    while ("+".equals(transitionStack.peekLast())) {//+ has equal or more priority
                        npi.add("+");
                        transitionStack.removeLast();
                    }
                    transitionStack.add("*");
                    break;
                case '(':
                    transitionStack.add("(");
                    break;
                case ')':
                    while (!"(".equals(transitionStack.peekLast())) {
                        npi.add(transitionStack.pollLast());
                    }
                    transitionStack.pollLast();//remove '('
                    break;
                default:
                    throw new IllegalStateException("Operator " + c + " unrecognised.");
            }
        }

        if (!"".equals(numberBuilder.toString())) {
            String number = numberBuilder.toString();
            npi.add(number);
        }

        while (Objects.nonNull(transitionStack.peekLast())) {
            npi.add(transitionStack.pollLast());
        }

        return npi;
    }

    enum Operator {
        ADD("+", BigInteger::add),
        TIME("*", BigInteger::multiply);

        private final String code;
        private final BiFunction<BigInteger, BigInteger, BigInteger> evaluator;

        Operator(String code, BiFunction<BigInteger, BigInteger, BigInteger> evaluator) {
            this.code = code;
            this.evaluator = evaluator;
        }

        public BigInteger evaluate(BigInteger a, BigInteger b) {
            return evaluator.apply(a, b);
        }

        public static Operator of(String code) {
            return Arrays.stream(values())
                         .filter(operator -> operator.code.equals(code))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Operator " + code + " is not recognised."));
        }
    }
}