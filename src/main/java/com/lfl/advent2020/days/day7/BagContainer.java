package com.lfl.advent2020.days.day7;

import com.lfl.advent2020.LinesConsumer;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.primitive.MutableObjectIntMap;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.factory.primitive.ObjectIntMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class BagContainer implements LinesConsumer {

    private final Map<String, Bag> bags = Maps.mutable.empty();
    @Getter
    private int shouldContain;
    @Getter
    private int canBeContainedBy;

    @Override
    public void consume(List<String> lines) {
        lines.forEach(this::parseLine);

        String bagColor = "shiny gold";
        Bag targetBag = bags.get(bagColor);
        Set<Bag> shinyGoldContainer = Sets.mutable.of(targetBag);
        int previousSize;
        do {
            previousSize = shinyGoldContainer.size();
            bags.values()
                .stream()
                .filter(bag -> Sets.intersect(bag.getCanContain().keySet(), shinyGoldContainer).size() > 0)
                .forEach(shinyGoldContainer::add);
        } while (previousSize < shinyGoldContainer.size());

        canBeContainedBy = shinyGoldContainer.size() - 1;
        log.info("canContainCount = {}", canBeContainedBy);
        shouldContain = targetBag.canContain();
        log.info("Shiny gold should contain {} bags", shouldContain);
    }

    private void parseLine(String line) {
        String[] split = line.replaceAll("\\.", "")
                             .replaceAll("bags", "")
                             .replaceAll("bag", "")
                             .replaceAll(" {2}", " ")
                             .trim()
                             .split(" contain ");
        String bagColor = split[0];
        Bag bag = bags.computeIfAbsent(bagColor, Bag::of);

        String[] otherBags = split[1].trim().split(", ");
        Arrays.stream(otherBags)
              .map(String::trim)
              .forEach(numberedBag -> {
                  if ("no other".equals(numberedBag)) {
                      return;
                  }

                  int nb = Integer.parseInt(numberedBag.substring(0, 1).trim());
                  String color = numberedBag.substring(2).trim();
                  Bag containedBag = bags.computeIfAbsent(color, Bag::of);
                  bag.putBag(containedBag, nb);
              });
    }

    @Data
    private static class Bag {
        private String color;

        private MutableObjectIntMap<Bag> canContain = ObjectIntMaps.mutable.empty();

        public static Bag of(String color) {
            Bag bag = new Bag();
            bag.setColor(color);
            return bag;
        }

        public void putBag(Bag bag, int nb) {
            canContain.put(bag, nb);
        }

        public int canContain() {
            return ((int) canContain.sum()) + canContain.keySet()
                                                        .stream()
                                                        .mapToInt(bag -> bag.canContain() * canContain.get(bag))
                                                        .sum();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Bag bag = (Bag) o;
            return color.equals(bag.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(color);
        }
    }
}
