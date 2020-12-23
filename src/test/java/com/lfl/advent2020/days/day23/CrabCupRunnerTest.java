package com.lfl.advent2020.days.day23;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class CrabCupRunnerTest {


    @Test
    public void test() {
        //Given
        CrabCupRunner service = new CrabCupRunner();
        MutableList<String> lines = Lists.mutable.of("389125467");
        service.setMaxMoveNbr(10);
        service.setNbrMax(9);
        service.setPart2(false);

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void test2() {
        //Given
        CrabCupRunner service = new CrabCupRunner();
        MutableList<String> lines = Lists.mutable.of("389125467");
        service.setMaxMoveNbr(100);
        service.setNbrMax(9);
        service.setPart2(false);

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void part1() {
        //Given
        CrabCupRunner service = new CrabCupRunner();
        MutableList<String> lines = Lists.mutable.of("716892543");
        service.setMaxMoveNbr(100);
        service.setNbrMax(9);
        service.setPart2(false);

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void part2() {
        //Given
        CrabCupRunner service = new CrabCupRunner();
        MutableList<String> lines = Lists.mutable.of("716892543");
        service.setMaxMoveNbr(10_000_000);
        service.setNbrMax(1_000_000);
        service.setPart2(true);

        //When
        service.consume(lines);

        //Then
    }
}