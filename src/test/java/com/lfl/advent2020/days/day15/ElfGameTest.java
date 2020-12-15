package com.lfl.advent2020.days.day15;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class ElfGameTest {

    @Test
    public void test() {
        //Given
        ElfGame service = new ElfGame();
        service.setMaxTurn(2020);
        MutableList<String> lines = Lists.mutable.of("0,3,6");
        int expectedResult = 436;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getResult()).isEqualTo(expectedResult);
    }

    @Test
    public void testPart1() {
        //Given
        ElfGame service = new ElfGame();
        service.setMaxTurn(2020);
        MutableList<String> lines = Lists.mutable.of("0,3,1,6,7,5");
        int expectedResult = 852;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getResult()).isEqualTo(expectedResult);
    }

    @Test
    public void testPart2() {
        //Given
        ElfGame service = new ElfGame();
        service.setMaxTurn(30000000);
        MutableList<String> lines = Lists.mutable.of("0,3,1,6,7,5");
        int expectedResult = 6007666;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getResult()).isEqualTo(expectedResult);
    }
}