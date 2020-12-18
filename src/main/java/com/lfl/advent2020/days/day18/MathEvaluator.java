package com.lfl.advent2020.days.day18;

import com.lfl.advent2020.LinesConsumer;
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
    @Override
    public void consume(List<String> lines) {
        BigInteger sum = lines.stream()
                              .peek(line -> log.info("line = {}", line))
                              .map(line -> line.replaceAll(" ", ""))
                              .map(this::evaluate1)
                              .reduce(BigInteger::add)
                              .orElse(BigInteger.ZERO);

        log.info("sum = {}", sum);

        BigInteger sum2 = lines.stream()
                              .peek(line -> log.info("line = {}", line))
                              .map(line -> line.replaceAll(" ", ""))
                              .map(this::evaluate2)
                              .reduce(BigInteger::add)
                              .orElse(BigInteger.ZERO);

        log.info("sum2 = {}", sum2);


    }

    public BigInteger evaluate2(String line) {
        List<String> npi = toNpi(line);

        log.info("NPI = {}", npi);
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

    private List<String> toNpi(String line) {
        List<String> npi = Lists.mutable.empty();
        Deque<String> transitionStack = new ArrayDeque<>();
        StringBuilder number = new StringBuilder();
        for (int index = 0; index < line.length(); index++) {
            char c = line.charAt(index);
            if (Character.isDigit(c)) {
                number.append(c);
                continue;
            } else {
                if (!"".equals(number.toString())) {
                    String numberS = number.toString();
                    number = new StringBuilder();
                    npi.add(numberS);
                }
            }

            if (c == '+') {
                transitionStack.add("+");
            }

            if (c == '*') {
                while ("+".equals(transitionStack.peekLast())) {
                    npi.add("+");
                    transitionStack.removeLast();
                }
                transitionStack.add("*");
            }

            if (c == '(') {
                transitionStack.add("(");
            }

            if (c == ')') {
                while (!"(".equals(transitionStack.peekLast())) {
                    npi.add(transitionStack.pollLast());
                }
                transitionStack.pollLast();//remove '('
            }
        }

        if (!"".equals(number.toString())) {
            String numberS = number.toString();
            npi.add(numberS);
        }
        while (Objects.nonNull(transitionStack.peekLast())) {
            npi.add(transitionStack.pollLast());
        }
        return npi;
    }

    public BigInteger evaluate1(String line) {
        StringBuilder number = new StringBuilder();
        Deque<String> stack = new ArrayDeque<>();
        for (int index = 0; index < line.length(); index++) {
            char c = line.charAt(index);
            if (Character.isDigit(c)) {
                number.append(c);
                continue;
            } else {
                if (!"".equals(number.toString())) {
                    String numberS = number.toString();
                    number = new StringBuilder();
                    evaluate1OrAdd(stack, numberS);
                }
            }

            if (c != ')') {
                stack.add(Character.toString(c));
            } else {
                String numberS = stack.pollLast();
                String openParenthesis = stack.pollLast();
                evaluate1OrAdd(stack, numberS);
            }
        }

        if (!"".equals(number.toString())) {
            String numberS = number.toString();
            evaluate1OrAdd(stack, numberS);
        }

        return new BigInteger(stack.pop());
    }

    private void evaluate1OrAdd(Deque<String> stack, String numberS) {
        if (stack.size() > 1) {
            evaluate1(stack, numberS);
        } else {
            stack.add(numberS);
        }
    }

    private void evaluate1(Deque<String> stack, String numberS) {
        String operator = stack.pollLast();
        String lastNumber = stack.pollLast();

        if ("(".equals(operator)) {
            stack.add(lastNumber);
            stack.add(operator);
            stack.add(numberS);
        } else {

            BigInteger evaluate = Operator.of(operator)
                                          .evaluate(new BigInteger(numberS), new BigInteger(lastNumber));
            stack.add(evaluate.toString());
        }
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
