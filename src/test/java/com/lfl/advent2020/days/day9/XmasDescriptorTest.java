package com.lfl.advent2020.days.day9;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

public class XmasDescriptorTest {

    @Test
    public void test() {
        //Given
        XmasDescriptor service = new XmasDescriptor();
        service.setSize(5);
        List<String> lines = Lists.mutable.of("35",
                                              "20",
                                              "15",
                                              "25",
                                              "47",
                                              "40",
                                              "62",
                                              "55",
                                              "65",
                                              "95",
                                              "102",
                                              "117",
                                              "150",
                                              "182",
                                              "127",
                                              "219",
                                              "299",
                                              "277",
                                              "309",
                                              "576");
        BigInteger expectedFirstNotSum = BigInteger.valueOf(127);
        BigInteger expectedWeakness = BigInteger.valueOf(62);

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getFirstNotSum()).isEqualTo(expectedFirstNotSum);
        Assertions.assertThat(service.getWeakness()).isEqualTo(expectedWeakness);
    }
}