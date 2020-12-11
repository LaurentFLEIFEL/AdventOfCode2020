package com.lfl.advent2020.days.day11;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class GameOfSeatTest {


    @Test
    public void test() {
        //Given
        MutableList<String> lines = Lists.mutable.of("L.LL.LL.LL",
                                                     "LLLLLLL.LL",
                                                     "L.L.L..L..",
                                                     "LLLL.LL.LL",
                                                     "L.LL.LL.LL",
                                                     "L.LLLLL.LL",
                                                     "..L.L.....",
                                                     "LLLLLLLLLL",
                                                     "L.LLLLLL.L",
                                                     "L.LLLLL.LL");
        GameOfSeat service = new GameOfSeat();
        int expectedCount = 26;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getCount()).isEqualTo(expectedCount);
    }
}