package com.lfl.advent2020.days.day12;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Test;

import java.util.List;

public class FerryNavigatorTest {

    @Test
    public void test() {
        //Given
        List<String> lines = Lists.mutable.of("F10",
                                              "N3",
                                              "F7",
                                              "R90",
                                              "F11");
        FerryNavigator service = new FerryNavigator();
        int expectedManhattanDistance = 286;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getFinalPosition().module1()).isEqualTo(expectedManhattanDistance);
    }
}