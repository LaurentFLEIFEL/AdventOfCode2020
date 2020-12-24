package com.lfl.advent2020.days.day24;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.tuple.Tuples;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;

@Slf4j
@Service
public class HexTilingManager implements LinesConsumer {

    @Setter
    private int dayMax = 100;

    @Override
    public void consume(List<String> lines) {

        List<List<Direction>> tileDirections = parseInput(lines);
        MutableMap<Point, Integer> flippedTiles = day0(tileDirections);
        MutableSet<Point> blackTiles = flippedTiles.select((point, flippedNbr) -> flippedNbr % 2 == 1)
                                                   .keysView()
                                                   .toSet();
        long count = blackTiles.size();
        log.info("Day 0: {}", count);

        MutableMap<Point, Color> map = buildMap(blackTiles);

        //printMap(map);
        for (int day = 1; day <= dayMax; day++) {
            map = addNeighbors(map);
            map = updateColors(map);
            count = map.count(color -> color == Color.BLACK);
            log.info("Day {}: {}", day, count);

            //printMap(map);
        }
    }

    private MutableMap<Point, Color> buildMap(MutableSet<Point> blackTiles) {
        IntSummaryStatistics xStatistics = blackTiles.stream()
                                                     .mapToInt(point -> point.x)
                                                     .summaryStatistics();

        IntSummaryStatistics yStatistics = blackTiles.stream()
                                                     .mapToInt(point -> point.y)
                                                     .summaryStatistics();

        MutableMap<Point, Color> map = Maps.mutable.empty();

        IntStream.range(xStatistics.getMin(), xStatistics.getMax() + 1)
                 .forEach(x -> IntStream.range(yStatistics.getMin(), yStatistics.getMax() + 1)
                                        .mapToObj(y -> Point.of(x, y))
                                        .map(point -> (blackTiles.contains(point)) ?
                                                      Tuples.pair(point, Color.BLACK) :
                                                      Tuples.pair(point, Color.WHITE))
                                        .forEach(pair -> map.put(pair.getOne(), pair.getTwo())));

        return map;
    }

    private void printMap(MutableMap<Point, Color> map) {
        IntSummaryStatistics xStatistics2 = map.keySet().stream()
                                               .mapToInt(point -> point.x)
                                               .summaryStatistics();

        System.out.println("xStatistics2 = " + xStatistics2);

        IntSummaryStatistics yStatistics2 = map.keySet().stream()
                                               .mapToInt(point -> point.y)
                                               .summaryStatistics();

        System.out.println("yStatistics2 = " + yStatistics2);

        for (int y = yStatistics2.getMax(); y >= yStatistics2.getMin(); y--) {
            for (int x = xStatistics2.getMin(); x <= xStatistics2.getMax(); x++) {
                System.out.print(map.getOrDefault(Point.of(x, y), Color.WHITE).getCode());
            }
            System.out.println();
        }
    }

    private MutableMap<Point, Color> updateColors(MutableMap<Point, Color> map) {
        MutableMap<Point, Color> tmp = Maps.mutable.withInitialCapacity(map.size());
        map.forEachKeyValue((point, color) -> {
            long count = findNeighbors(point).stream()
                                             .filter(map::containsKey)
                                             .map(map::get)
                                             .filter(color1 -> color1 == Color.BLACK)
                                             .count();

            tmp.put(point, color.shouldSwap(count) ? color.swap() : color);
        });

        return tmp;
    }

    private MutableMap<Point, Color> addNeighbors(MutableMap<Point, Color> map) {
        MutableMap<Point, Color> tmp = Maps.mutable.withInitialCapacity(map.size());

        map.forEachKeyValue((point, color) -> {
            tmp.put(point, color);
            findNeighbors(point).select(Predicates.not(map::containsKey))
                                .forEach(neighbor -> tmp.putIfAbsent(neighbor, Color.WHITE));
        });

        return tmp;
    }

    private final MutableMap<Point, MutableSet<Point>> memoizedNeighbors = Maps.mutable.empty();

    private MutableSet<Point> findNeighbors(Point start) {
        return memoizedNeighbors.computeIfAbsent(start, this::doFindNeighbors);
    }

    private MutableSet<Point> doFindNeighbors(Point start) {
        return Arrays.stream(Direction.values())
                     .map(direction -> direction.move(start))
                     .collect(Collectors2.toSet());
    }

    private MutableMap<Point, Integer> day0(List<List<Direction>> tileDirections) {
        MutableMap<Point, Integer> flippedTiles = Maps.mutable.empty();
        tileDirections.stream()
                      .map(directions -> directions.stream()
                                                   .map((Function<Direction, Function<Point, Point>>) direction -> direction::move)
                                                   .reduce(Function::andThen)
                                                   .orElse(Function.identity())
                                                   .apply(Point.ZERO))
                      .forEach(point -> flippedTiles.merge(point, 1, Integer::sum));
        return flippedTiles;
    }

    private List<List<Direction>> parseInput(List<String> lines) {
        List<List<Direction>> tileDirections = Lists.mutable.empty();
        for (String line : lines) {
            String[] split = line.split("");
            String previous = "";
            List<Direction> directions = Lists.mutable.empty();
            for (String s : split) {
                if ("n".equals(s) || "s".equals(s)) {
                    previous = s;
                    continue;
                }

                if (!previous.isEmpty()) {
                    s = previous + s;
                    previous = "";
                }

                directions.add(Direction.of(s));
            }
            tileDirections.add(directions);
        }
        return tileDirections;
    }

    public enum Direction {
        EAST("e", p -> Point.of(p.x + 1, p.y)),
        SOUTH_EAST("se", p -> Point.of(p.x + 1, p.y - 1)),
        SOUTH_WEST("sw", p -> Point.of(p.x, p.y - 1)),
        WEST("w", p -> Point.of(p.x - 1, p.y)),
        NORTH_WEST("nw", p -> Point.of(p.x - 1, p.y + 1)),
        NORTH_EAST("ne", p -> Point.of(p.x, p.y + 1));

        private final String code;
        private final Function<Point, Point> mover;

        Direction(String code, Function<Point, Point> mover) {
            this.code = code;
            this.mover = mover;
        }

        public static Direction of(String code) {
            return Arrays.stream(values())
                         .filter(direction -> direction.code.equals(code))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Direction " + code + " is not recognised."));
        }

        public Point move(Point p) {
            return mover.apply(p);
        }
    }

    public enum Color {
        WHITE(".", n -> n == 2),
        BLACK("#", n -> n == 0 || n > 2);

        @Getter
        private final String code;
        private final LongPredicate shouldSwap;

        Color(String code, LongPredicate shouldSwap) {
            this.code = code;
            this.shouldSwap = shouldSwap;
        }

        public Color swap() {
            return (this == WHITE) ? BLACK : WHITE;
        }

        public boolean shouldSwap(long n) {
            return shouldSwap.test(n);
        }
    }
}
