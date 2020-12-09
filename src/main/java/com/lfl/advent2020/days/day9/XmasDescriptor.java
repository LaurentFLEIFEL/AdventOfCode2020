package com.lfl.advent2020.days.day9;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class XmasDescriptor implements LinesConsumer {

    @Setter
    private int size = 25;
    @Getter
    private BigInteger weakness;
    @Getter
    private BigInteger firstNotSum;

    @Override
    public void consume(List<String> lines) {
        List<BigInteger> numbers = lines.stream()
                                        .map(BigInteger::new)
                                        .collect(Collectors.toList());

        firstNotSum = findFirstNotSum(numbers);
        log.info("First number not sum = {}", firstNotSum);

        MutableList<BigInteger> contiguousNbr = findContiguousSumTo(numbers);
        weakness = contiguousNbr.min().add(contiguousNbr.max());
        log.info("Weakness = {}", weakness);
    }

    private MutableList<BigInteger> findContiguousSumTo(List<BigInteger> numbers) {
        MutableList<BigInteger> contiguousNbr = Lists.mutable.empty();
        for (int index = 0; index < numbers.size(); index++) {
            contiguousNbr = Lists.mutable.withInitialCapacity(10);
            BigInteger sum = BigInteger.ZERO;
            int count = 0;
            while (sum.compareTo(firstNotSum) < 0) {
                BigInteger number = numbers.get(index + count);
                sum = sum.add(number);
                contiguousNbr.add(number);
                count++;
            }

            if (sum.equals(firstNotSum)) {
                break;
            }
        }
        return contiguousNbr;
    }

    //First way to do it with queue

    private BigInteger findFirstNotSum(List<BigInteger> lines) {
        Deque<BigInteger> queue = new ArrayDeque<>(size);

        for (BigInteger number : lines) {
            if (queue.size() >= size) {
                if (!isSumOf(queue, number)) {
                    return number;
                }

                queue.pollFirst();
            }
            queue.add(number);
        }
        return BigInteger.ZERO;
    }

    private static boolean isSumOf(Deque<BigInteger> queue, BigInteger number) {
        List<BigInteger> numbers = new ArrayList<>(queue);
        for (int i = 0; i < numbers.size() - 1; i++) {
            BigInteger nb1 = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                BigInteger nb2 = numbers.get(j);
                if (nb1.add(nb2).equals(number)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Second way to do it by playing with indexes

    @SuppressWarnings("unused")
    private BigInteger findFirstNotSum2(List<BigInteger> numbers) {
        for (int index = size; index < numbers.size(); index++) {
            BigInteger number = numbers.get(index);
            if (!isSumOf(numbers, number, index - size)) {
                return number;
            }
        }

        return BigInteger.ZERO;
    }

    private boolean isSumOf(List<BigInteger> numbers, BigInteger number, int startIndex) {
        for (int i = startIndex; i < startIndex + size - 1; i++) {
            BigInteger nb1 = numbers.get(i);
            for (int j = i + 1; j < startIndex + size; j++) {
                BigInteger nb2 = numbers.get(j);
                if (nb1.add(nb2).equals(number)) {
                    return true;
                }
            }
        }
        return false;
    }
}
