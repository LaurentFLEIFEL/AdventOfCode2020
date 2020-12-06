package com.lfl.advent2020.days.day6;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Test;

import java.util.List;

public class CustomFromHandlerTest {

    CustomFromHandler service = new CustomFromHandler();

    @Test
    public void test() {
        //Given
        List<String> lines = Lists.mutable.of("abc",
                                              "",
                                              "a",
                                              "b",
                                              "c",
                                              "",
                                              "ab",
                                              "ac",
                                              "",
                                              "a",
                                              "a",
                                              "a",
                                              "a",
                                              "",
                                              "b");
        int expectedSum = 6;//11 for part 1

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum()).isEqualTo(expectedSum);
    }
}