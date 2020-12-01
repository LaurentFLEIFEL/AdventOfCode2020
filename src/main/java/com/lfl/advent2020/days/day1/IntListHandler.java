package com.lfl.advent2020.days.day1;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.springframework.stereotype.Service;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class IntListHandler implements LinesConsumer {

    private static final int SUM = 2020;

    @Override
    public void consume(List<String> lines) {
        log.info("Initial size = {}", lines.size());
        IntSummaryStatistics statistics = lines.stream()
                                               .mapToInt(Integer::parseInt)
                                               .summaryStatistics();
        int min = statistics.getMin();
        int max = statistics.getMax();

        MutableIntList numbers = lines.stream()
                                      .mapToInt(Integer::parseInt)
                                      .filter(n -> n + min <= SUM)
                                      .filter(n -> n + max >= SUM)
                                      .collect(IntLists.mutable::empty,
                                               MutableIntCollection::add,
                                               MutableIntCollection::addAll);

        Result result = findNumbers(numbers);
        if (Objects.isNull(result)) {
            log.error("Error, result is null.");
            return;
        }

        log.info("Result = {}", result.toProduct());
    }

    private Result findNumbers(MutableIntList numbers) {
        int number1;
        int number2;
        int number3;
        for (int index1 = 0; index1 < numbers.size() - 2; index1++) {
            for (int index2 = index1 + 1; index2 < numbers.size() - 1; index2++) {
                for (int index3 = index2 + 1; index3 < numbers.size(); index3++) {
                    number1 = numbers.get(index1);
                    number2 = numbers.get(index2);
                    number3 = numbers.get(index3);

                    if (number1 + number2 + number3 == SUM) {
                        log.info("{} + {} + {} = {}", number1, number2, number3, SUM);
                        return Result.of(number1, number2, number3);
                    }
                }
            }
        }
        return null;
    }

    @Getter
    @Setter
    private static class Result {
        private int number1;
        private int number2;
        private int number3;

        public static Result of(int number1, int number2, int number3) {
            Result result = new Result();
            result.setNumber1(number1);
            result.setNumber2(number2);
            result.setNumber3(number3);
            return result;
        }

        public int toProduct() {
            return number1 * number2 * number3;
        }
    }
}
