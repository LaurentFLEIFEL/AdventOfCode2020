package com.lfl.advent2020.days.day18;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

import java.math.BigInteger;

public class MathEvaluatorTest {

    @Test
    public void test() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("1 + 2 * 3 + 4 * 5 + 6");
        int expectedSum1 = 71;
        int expectedSum2 = 231;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum1()).isEqualTo(BigInteger.valueOf(expectedSum1));
        Assertions.assertThat(service.getSum2()).isEqualTo(BigInteger.valueOf(expectedSum2));
    }

    @Test
    public void test2() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("1 + (2 * 3) + (4 * (5 + 6))");
        int expectedSum1 = 51;
        int expectedSum2 = 51;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum1()).isEqualTo(BigInteger.valueOf(expectedSum1));
        Assertions.assertThat(service.getSum2()).isEqualTo(BigInteger.valueOf(expectedSum2));
    }

    @Test
    public void test3() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("(7 * (3 + 8 + 8 + 7) + (6 + 8 * 2 + 5 + 2 * 6) * (5 + 2) * 9) + ((7 * 4 + 8) * 6 * 8 + 9) * 7 * 2 * 2");
        int expectedSum1 = 740124;
        int expectedSum2 = 9896040;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum1()).isEqualTo(BigInteger.valueOf(expectedSum1));
        Assertions.assertThat(service.getSum2()).isEqualTo(BigInteger.valueOf(expectedSum2));
    }

    @Test
    public void test4() {
        //Given
        MathEvaluator service = new MathEvaluator();
        MutableList<String> lines = Lists.mutable.of("2 * 3 + (4 * 5)");
        int expectedSum1 = 26;
        int expectedSum2 = 46;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum1()).isEqualTo(BigInteger.valueOf(expectedSum1));
        Assertions.assertThat(service.getSum2()).isEqualTo(BigInteger.valueOf(expectedSum2));
    }
}