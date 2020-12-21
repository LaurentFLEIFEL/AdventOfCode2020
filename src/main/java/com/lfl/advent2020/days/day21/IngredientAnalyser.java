package com.lfl.advent2020.days.day21;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.collector.Collectors2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IngredientAnalyser implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        Map<String, Set<String>> ingredientByAllergen = Maps.mutable.empty();
        Map<String, Integer> occurrenceByIngredient = Maps.mutable.empty();
        Set<String> allergens = Sets.mutable.empty();

        for (String line : lines) {
            Food food = Food.of(line);
            allergens.addAll(food.getAllergens());

            for (String ingredient : food.getIngredients()) {
                occurrenceByIngredient.merge(ingredient, 1, Integer::sum);
            }
        }

        Set<String> allIngredients = occurrenceByIngredient.keySet();
        allergens.forEach(allergen -> ingredientByAllergen.put(allergen, Sets.mutable.withAll(allIngredients)));

        for (String line : lines) {
            Food food = Food.of(line);
            for (String allergen : food.getAllergens()) {
                for (String ingredient : allIngredients) {
                    if (!food.getIngredients().contains(ingredient)) {
                        ingredientByAllergen.get(allergen).remove(ingredient);
                    }
                }
            }
        }

        log.info("ingredientByAllergen = {}", ingredientByAllergen);
        ingredientByAllergen.forEach((key, value) -> log.info("{} = {}", key, value));

        MutableSet<String> allergenicIngredients = ingredientByAllergen.values()
                                                                       .stream()
                                                                       .flatMap(Set::stream)
                                                                       .collect(Collectors2.toSet());

        int sum = allIngredients
                .stream()
                .filter(ingredient -> !allergenicIngredients.contains(ingredient))
                .mapToInt(occurrenceByIngredient::get)
                .sum();

        log.info("Sum = {}", sum);

        Set<String> ingredientDetected = Sets.mutable.empty();
        while (ingredientDetected.size() != ingredientByAllergen.keySet().size()) {
            for (String allergen : ingredientByAllergen.keySet()) {
                Set<String> ingredients = ingredientByAllergen.get(allergen);
                if (ingredients.size() == 1) {
                    ingredientDetected.add(ingredients.iterator().next());
                    continue;
                }

                ingredientDetected.stream()
                                  .filter(ingredients::contains)
                                  .forEach(ingredients::remove);
            }

            log.info("ingredientDetected = {}", ingredientDetected);
        }

        ingredientByAllergen.forEach((key, value) -> log.info("{} = {}", key, value));

        String result = ingredientByAllergen.keySet()
                                             .stream()
                                             .sorted()
                                             .map(ingredientByAllergen::get)
                                             .flatMap(Set::stream)
                                             .collect(Collectors.joining(","));

        log.info("Result = {}", result);
    }

    @Getter
    public static class Food {
        MutableSet<String> ingredients;
        MutableSet<String> allergens;

        public static Food of(String line) {
            String[] split = line.split(" \\(contains ");
            String[] ingredients = split[0].split(" ");
            String[] allergens = split[1].substring(0, split[1].length() - 1).split(", ");

            Food food = new Food();
            food.ingredients = Arrays.stream(ingredients).collect(Collectors2.toSet());
            food.allergens = Arrays.stream(allergens).collect(Collectors2.toSet());
            return food;
        }
    }
}
