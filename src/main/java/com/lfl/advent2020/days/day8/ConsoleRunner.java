package com.lfl.advent2020.days.day8;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.IntConsumer;

@Slf4j
public class ConsoleRunner {
    public static final AtomicInteger accumulator = new AtomicInteger();

    public void run(List<Operation> operations) {
        log.info("Start run");

        accumulator.set(0);
        if (CollectionUtils.isEmpty(operations)) {
            return;
        }
        operations.forEach(Operation::resetCount);

        Operation operation = operations.get(0);
        while (operation.nextIndex() < operations.size()) {
            operation.run();
            operation = operations.get(operation.nextIndex());
        }
        operation.run();// last one
        log.info("End run");
    }

    public static class Operation implements Runnable {
        private final int index;
        @Getter
        @Setter
        private Operator operator;
        private final int argument;
        private int count;

        public Operation(int index, Operator operator, int argument) {
            this.index = index;
            this.operator = operator;
            this.argument = argument;
        }

        public static Operation of(int index, String line) {
            Operator operator = Operator.of(line.substring(0, 3));
            int argument = Integer.parseInt(line.substring(4));
            return new Operation(index, operator, argument);
        }

        @Override
        public void run() {
            count++;
            if (count == 2) {
                log.error("Accumulator = {}", accumulator);
                throw new IllegalStateException("Infinite loop");
            }
            //log.info("Running {} {}", operator, argument);
            operator.run(argument);
        }

        public int nextIndex() {
            return operator.nextIndex(index, argument);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Operation operation = (Operation) o;
            return index == operation.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }

        public void resetCount() {
            count = 0;
        }
    }

    public enum Operator {
        ACC("acc",
            1,
            accumulator::addAndGet,
            (i, value) -> i + 1),
        JMP("jmp",
            1,
            value -> {},
            Integer::sum),
        NOP("nop",
            1,
            value -> {},
            (i, value) -> i + 1);

        private final String code;
        @Getter
        private final int argumentNbr;
        private final IntConsumer runner;
        private final BiFunction<Integer, Integer, Integer> nextIndexComputer;

        Operator(String code,
                 int argumentNbr,
                 IntConsumer runner,
                 BiFunction<Integer, Integer, Integer> nextIndexComputer) {
            this.code = code;
            this.argumentNbr = argumentNbr;
            this.runner = runner;
            this.nextIndexComputer = nextIndexComputer;
        }

        public static Operator of(String code) {
            return Arrays.stream(values())
                         .filter(operator -> operator.code.equals(code))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Operator " + code + " is not recognised."));
        }

        public int nextIndex(int currentIndex, int argument) {
            return nextIndexComputer.apply(currentIndex, argument);
        }

        public void run(int argument) {
            runner.accept(argument);
        }

        public boolean isAcc() {
            return this == ACC;
        }

        public boolean isJmp() {
            return this == JMP;
        }

        @SuppressWarnings("unused")
        public boolean isNop() {
            return this == NOP;
        }
    }
}