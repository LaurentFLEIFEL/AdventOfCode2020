package com.lfl.advent2020;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.IntSummaryStatistics;
import java.util.List;

@Slf4j
public class MiscTest {

    @Test
    public void testday1_1() throws IOException, URISyntaxException {
        //Given
        List<String> lines = LinesConsumer.readAllInput("day1-1.txt");
        log.info("Initial size = {}", lines.size());

        //When
        IntSummaryStatistics statistics = lines.stream()
                                               .mapToInt(Integer::parseInt)
                                               .summaryStatistics();
        int min = statistics.getMin();
        int max = statistics.getMax();

        log.info("min = {}", min);
        log.info("max = {}", max);

        long count = lines.stream()
                          .mapToInt(Integer::parseInt)
                          .filter(n -> n + min <= 2020)
                          .count();

        log.info("New size = {}", count);

        long count2 = lines.stream()
                           .mapToInt(Integer::parseInt)
                           .filter(n -> n + max >= 2020)
                           .count();

        log.info("New size = {}", count2);

        long count3 = lines.stream()
                           .mapToInt(Integer::parseInt)
                           .filter(n -> n + min <= 2020)
                           .filter(n -> n + max >= 2020)
                           .count();

        log.info("New size = {}", count3);

        MutableIntList numbers = lines.stream()
                                      .mapToInt(Integer::parseInt)
                                      .filter(n -> n + min <= 2020)
                                      .filter(n -> n + max >= 2020)
                                      .collect(IntLists.mutable::empty,
                                               MutableIntCollection::add,
                                               MutableIntCollection::addAll);

        for (int i = 0; i < numbers.size() - 1; i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                int i1 = numbers.get(i);
                int j1 = numbers.get(j);

                if (i1 + j1 == 2020) {
                    log.info("{} + {} = 2020", i1, j1);
                }
            }
        }
        //Then
    }
}
