package com.lfl.advent2020.days.day16;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class TicketValidatorTest {


    @Test
    public void test() {
        //Given
        TicketValidator service = new TicketValidator();
        service.setDoPart2(false);
        MutableList<String> lines = Lists.mutable.of("class: 1-3 or 5-7",
                                                     "row: 6-11 or 33-44",
                                                     "seat: 13-40 or 45-50",
                                                     "",
                                                     "your ticket:",
                                                     "7,1,14",
                                                     "",
                                                     "nearby tickets:",
                                                     "7,3,47",
                                                     "40,4,50",
                                                     "55,2,20",
                                                     "38,6,12");
        int expectedTicketErrorRate = 71;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getTicketErrorRate()).isEqualTo(expectedTicketErrorRate);
    }

    @Test
    public void test2() {
        //Given
        TicketValidator service = new TicketValidator();
        MutableList<String> lines = Lists.mutable.of("class: 0-1 or 4-19",
                                                     "row: 0-5 or 8-19",
                                                     "seat: 0-13 or 16-19",
                                                     "",
                                                     "your ticket:",
                                                     "11,12,13",
                                                     "",
                                                     "nearby tickets:",
                                                     "3,9,18",
                                                     "15,1,5",
                                                     "5,14,9");
        int expectedRowFieldNumber = 0;
        int expectedClassFieldNumber = 1;
        int expectedSeatFieldNumber = 2;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getRules()
                                     .detect(rule -> rule.getName().equals("row"))
                                     .getFieldNumber())
                  .isEqualTo(expectedRowFieldNumber);
        Assertions.assertThat(service.getRules()
                                     .detect(rule -> rule.getName().equals("class"))
                                     .getFieldNumber())
                  .isEqualTo(expectedClassFieldNumber);
        Assertions.assertThat(service.getRules()
                                     .detect(rule -> rule.getName().equals("seat"))
                                     .getFieldNumber())
                  .isEqualTo(expectedSeatFieldNumber);
    }
}