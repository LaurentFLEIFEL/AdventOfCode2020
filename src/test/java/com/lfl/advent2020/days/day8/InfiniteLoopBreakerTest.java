package com.lfl.advent2020.days.day8;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Test;

import java.util.List;

public class InfiniteLoopBreakerTest {

    @Test
    public void test() {
        //Given
        InfiniteLoopBreaker service = new InfiniteLoopBreaker();
        List<String> lines = Lists.mutable.of("nop +0",
                                              "acc +1",
                                              "jmp +4",
                                              "acc +3",
                                              "jmp -3",
                                              "acc -99",
                                              "acc +1",
                                              "jmp -4",
                                              "acc +6");
        int expectedAccumulator = 8;
        int expectedFirstAccumulation = 5;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(ConsoleRunner.accumulator.get()).isEqualTo(expectedAccumulator);
        Assertions.assertThat(service.getFirstAccumulation()).isEqualTo(expectedFirstAccumulation);
    }
}