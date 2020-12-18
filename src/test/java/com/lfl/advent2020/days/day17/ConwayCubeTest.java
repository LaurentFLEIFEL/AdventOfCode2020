package com.lfl.advent2020.days.day17;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class ConwayCubeTest {

    @Test
    public void test() {
        //Given
        ConwayCube service = new ConwayCube();
        MutableList<String> lines = Lists.mutable.of(".#.",
                                                     "..#",
                                                     "###");

        //When
        service.consume(lines);

        //Then
    }
}