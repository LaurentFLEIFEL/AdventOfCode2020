package com.lfl.advent2020.days.day13;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

import java.math.BigInteger;

public class ShuttleAirportTest {

    @Test
    public void test() {
        //Given
        ShuttleAirport service = new ShuttleAirport();
        MutableList<String> lines = Lists.mutable.of("939",
                                                     "7,13,x,x,59,x,31,19");
        int expectedResult = 295;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getResult1()).isEqualTo(expectedResult);
    }

    @Test
    public void test2() {
        //Given
        ShuttleAirport service = new ShuttleAirport();
        MutableList<String> lines = Lists.mutable.of("939",
                                                     "17,x,13,19");
        BigInteger expectedResult = BigInteger.valueOf(3417);

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getResult2()).isEqualTo(expectedResult);
    }
}