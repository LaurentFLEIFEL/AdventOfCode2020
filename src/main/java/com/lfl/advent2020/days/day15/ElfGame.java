package com.lfl.advent2020.days.day15;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ElfGame implements LinesConsumer {

    private final MutableMap<Integer, List<Integer>> numberSeen = Maps.mutable.empty();

    @Setter
    private int maxTurn = 30000000;
    @Getter
    private int result;
    private int[] firstNumbers;

    @Override
    public void consume(List<String> lines) {
        int previous = -1;
        firstNumbers = Arrays.stream(lines.get(0).split(","))
                             .mapToInt(Integer::parseInt)
                             .toArray();
        for (int turn = 0; turn < maxTurn; turn++) {
            previous = computePrevious(previous, turn);
            numberSeen.computeIfAbsent(previous, i -> Lists.mutable.empty())
                      .add(turn);
        }

        result = previous;
        log.info("Result = {}", result);
    }

    private int computePrevious(int previous, int turn) {
        //starting numbers
        if (turn < firstNumbers.length) {
            return firstNumbers[turn];
        }

        if (numberSeen.get(previous).size() == 1) {
            return 0;
        }

        List<Integer> turnsSpoken = numberSeen.get(previous);
        int size = turnsSpoken.size();
        int last = turnsSpoken.get(size - 1);
        int previousLast = turnsSpoken.get(size - 2);
        return last - previousLast;
    }
}
