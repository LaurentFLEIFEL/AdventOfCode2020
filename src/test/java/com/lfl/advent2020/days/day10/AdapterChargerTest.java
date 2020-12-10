package com.lfl.advent2020.days.day10;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

import java.math.BigInteger;

public class AdapterChargerTest {

    @Test
    public void test1() {
        //Given
        AdapterCharger service = new AdapterCharger();
        MutableList<String> lines = Lists.mutable.of("16",
                                                     "10",
                                                     "15",
                                                     "5",
                                                     "1",
                                                     "11",
                                                     "7",
                                                     "19",
                                                     "6",
                                                     "12",
                                                     "4");
        int expectedProduct = 35;
        BigInteger expectedArrangements = BigInteger.valueOf(8);

        //When
        service.consume(lines);
        int actualProduct = service.getCount1() * service.getCount3();

        //Then
        Assertions.assertThat(actualProduct).isEqualTo(expectedProduct);
        Assertions.assertThat(service.getArrangements()).isEqualTo(expectedArrangements);
    }

    @Test
    public void test2() {
        //Given
        AdapterCharger service = new AdapterCharger();
        MutableList<String> lines = Lists.mutable.of("28",
                                                     "33",
                                                     "18",
                                                     "42",
                                                     "31",
                                                     "14",
                                                     "46",
                                                     "20",
                                                     "48",
                                                     "47",
                                                     "24",
                                                     "23",
                                                     "49",
                                                     "45",
                                                     "19",
                                                     "38",
                                                     "39",
                                                     "11",
                                                     "1",
                                                     "32",
                                                     "25",
                                                     "35",
                                                     "8",
                                                     "17",
                                                     "7",
                                                     "9",
                                                     "4",
                                                     "2",
                                                     "34",
                                                     "10",
                                                     "3");
        int expectedProduct = 220;
        BigInteger expectedArrangements = BigInteger.valueOf(19208);

        //When
        service.consume(lines);
        int actualProduct = service.getCount1() * service.getCount3();

        //Then
        Assertions.assertThat(actualProduct).isEqualTo(expectedProduct);
        Assertions.assertThat(service.getArrangements()).isEqualTo(expectedArrangements);
    }
}