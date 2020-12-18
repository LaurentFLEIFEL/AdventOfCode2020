package com.lfl.advent2020.days.day18;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class MathEvaluatorTest {

    @Test
    public void test() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("1 + 2 * 3 + 4 * 5 + 6");

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void test2() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("1 + (2 * 3) + (4 * (5 + 6))");

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void test3() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("(7 * (3 + 8 + 8 + 7) + (6 + 8 * 2 + 5 + 2 * 6) * (5 + 2) * 9) + ((7 * 4 + 8) * 6 * 8 + 9) * 7 * 2 * 2");

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void test4() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("2 * 3 + (4 * 5)");

        //When
        service.consume(lines);

        //Then
    }
}