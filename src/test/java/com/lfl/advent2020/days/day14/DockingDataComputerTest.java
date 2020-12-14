package com.lfl.advent2020.days.day14;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockingDataComputerTest {

    @Test
    public void test() {
        //Given
        DockingDataComputer service = new DockingDataComputer();
        service.setDoPart2(false);
        MutableList<String> lines = Lists.mutable.of("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X",
                                                     "mem[8] = 11",
                                                     "mem[7] = 101",
                                                     "mem[8] = 0");
        int expectedSum = 165;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum1()).isEqualTo(expectedSum);
    }

    @Test
    public void test2() {
        //Given
        DockingDataComputer service = new DockingDataComputer();
        service.setDoPart1(false);
        MutableList<String> lines = Lists.mutable.of("mask = 000000000000000000000000000000X1001X",
                                                     "mem[42] = 100",
                                                     "mask = 00000000000000000000000000000000X0XX",
                                                     "mem[26] = 1");
        int expectedSum = 208;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getSum2()).isEqualTo(expectedSum);
    }

    @Test
    public void name() {
        //Given
        Pattern memoryPattern = Pattern.compile("mem\\[(?<address>\\d+)] = (?<value>\\d+)");
        String input = "mem[8] = 11";
        int expectedAddress = 8;
        int expectedValue = 11;

        //When
        Matcher matcher = memoryPattern.matcher(input);
        matcher.find();
        int address = Integer.parseInt(matcher.group("address"));
        int value = Integer.parseInt(matcher.group("value"));

        //Then
        Assertions.assertThat(address).isEqualTo(expectedAddress);
        Assertions.assertThat(value).isEqualTo(expectedValue);
    }
}