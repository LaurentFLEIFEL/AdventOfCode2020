package com.lfl.advent2020.days.day8;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.days.day8.ConsoleRunner.Operation;
import com.lfl.advent2020.days.day8.ConsoleRunner.Operator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class InfiniteLoopBreaker implements LinesConsumer {

    @Getter
    private final ConsoleRunner consoleRunner = new ConsoleRunner();

    @Override
    public void consume(List<String> lines) {
        List<Operation> operations = IntStream.range(0, lines.size())
                                              .mapToObj(index -> Operation.of(index, lines.get(index)))
                                              .collect(Collectors.toList());

        fixOperations(operations);
        log.info("Accumulator = {}", ConsoleRunner.accumulator);
    }

    public void fixOperations(List<Operation> operations) {
        int index = -1;
        while (true) {
            boolean hasError = tryRun(operations);
            if (!hasError) {
                break;
            }

            if (index >= 0) {
                Operation operation = operations.get(index);
                operation.setOperator(swapNopJmp(operation.getOperator()));
            }

            index = findNextIndex(operations, index);

            Operation operation = operations.get(index);
            operation.setOperator(swapNopJmp(operation.getOperator()));
        }
    }

    private int findNextIndex(List<Operation> operations, int index) {
        index++;
        while (operations.get(index).getOperator().isAcc()) {
            index++;
        }
        return index;
    }

    private boolean tryRun(List<Operation> operations) {
        try {
            consoleRunner.run(operations);
        } catch (IllegalStateException e) {
            return true;
        }
        return false;
    }

    public static Operator swapNopJmp(Operator operator) {
        if (operator.isJmp()) {
            return Operator.NOP;
        }
        return Operator.JMP;
    }
}
