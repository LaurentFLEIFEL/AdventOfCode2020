package com.lfl.advent2020.days.day3;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class TobogganDriverTest {

    private final static TobogganDriver service = new TobogganDriver();

    @Test
    public void test1() {
        //Given
        int expected = 336;
        MutableList<String> lines = Lists.mutable.of("..##.......",
                                                     "#...#...#..",
                                                     ".#....#..#.",
                                                     "..#.#...#.#",
                                                     ".#...##..#.",
                                                     "..#.##.....",
                                                     ".#.#.#....#",
                                                     ".#........#",
                                                     "#.##...#...",
                                                     "#...##....#",
                                                     ".#..#...#.#");

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getProduct().intValue()).isEqualTo(expected);
    }
}