package com.lfl.advent2020.days.day22;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class SpaceCardsRunnerTest {

    @Test
    public void test() {
        //Given
        SpaceCardsRunner service = new SpaceCardsRunner();
        MutableList<String> lines = Lists.mutable.of("Player 1:",
                                                     "9",
                                                     "2",
                                                     "6",
                                                     "3",
                                                     "1",
                                                     "",
                                                     "Player 2:",
                                                     "5",
                                                     "8",
                                                     "4",
                                                     "7",
                                                     "10");

        //When
        service.consume(lines);

        //Then
    }

}