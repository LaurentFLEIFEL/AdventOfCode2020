package com.lfl.advent2020.days.day21;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class IngredientAnalyserTest {

    @Test
    public void test() {
        //Given
        IngredientAnalyser service = new IngredientAnalyser();
        MutableList<String> lines = Lists.mutable.of("mxmxvkd kfcds sqjhc nhms (contains dairy, fish)",
                                                     "trh fvjkl sbzzf mxmxvkd (contains dairy)",
                                                     "sqjhc fvjkl (contains soy)",
                                                     "sqjhc mxmxvkd sbzzf (contains fish)");

        //When
        service.consume(lines);

        //Then
    }
}