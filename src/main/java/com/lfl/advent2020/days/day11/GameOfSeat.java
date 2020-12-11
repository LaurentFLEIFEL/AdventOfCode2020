package com.lfl.advent2020.days.day11;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static com.lfl.advent2020.days.day11.GameOfSeat.SeatType.EMPTY;
import static com.lfl.advent2020.days.day11.GameOfSeat.SeatType.FLOOR;
import static com.lfl.advent2020.days.day11.GameOfSeat.SeatType.OCCUPIED;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class GameOfSeat implements LinesConsumer {

    private int height;
    private int width;
    private Map<Point, SeatType> map;
    private int distanceMax;
    @Getter
    private long count;

    @Override
    public void consume(List<String> lines) {
        height = lines.size();
        width = lines.get(0).length();
        distanceMax = Math.max(height, width);
        map = buildMap(lines);

        runGenerations();

        count = map.values()
                   .stream()
                   .filter(type -> type == OCCUPIED)
                   .count();

        log.info("count = {}", count);
    }

    private void runGenerations() {
        Map<Point, SeatType> nextMap = map;

        do {
            map = nextMap;
            nextMap = Maps.mutable.withInitialCapacity(map.size());
            for (Point point : map.keySet()) {
                nextMap.put(point, computeNextType(point));
            }
        } while (!map.equals(nextMap));
    }

    private SeatType computeNextType(Point start) {
        SeatType seatType = map.get(start);
        int occupiedNeighborSeats = numberOfOccupiedNeighborSeats(start);
        if (seatType == EMPTY && seatType.shouldChange(occupiedNeighborSeats)) {
            return OCCUPIED;
        }
        if (seatType == OCCUPIED && seatType.shouldChange(occupiedNeighborSeats)) {
            return EMPTY;
        }

        return seatType;
    }

    private int numberOfOccupiedNeighborSeats(Point start) {
        return (int) findNeighbors(start).stream()
                                         .map(directions -> directions.stream()
                                                                      .map(point -> map.get(point))
                                                                      .filter(type -> type != FLOOR)
                                                                      .findFirst()
                                                                      .orElse(FLOOR))
                                         .filter(type -> type == OCCUPIED)
                                         .count();
    }

    private final Map<Point, List<List<Point>>> memoizedNeighbors = Maps.mutable.empty();

    private List<List<Point>> findNeighbors(Point start) {
        return memoizedNeighbors.computeIfAbsent(start, this::doFindNeighbors);
    }

    private List<List<Point>> doFindNeighbors(Point start) {
        return Arrays.stream(Direction.values())
                     .map(direction -> direction.computeDirection(start, distanceMax)//distanceMax == 1 for part 1
                                                .stream()
                                                .filter(point -> map.containsKey(point))
                                                .collect(toList()))
                     .filter(list -> !list.isEmpty())
                     .collect(toList());
    }

    private Map<Point, SeatType> buildMap(List<String> lines) {
        Map<Point, SeatType> map = Maps.mutable.withInitialCapacity(height * width);

        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) {
                char type = line.charAt(j);
                map.put(Point.of(i, j), SeatType.of(type));
            }
        }
        return map;
    }

    public enum SeatType {
        FLOOR('.', i -> false),
        EMPTY('L', i -> i == 0),
        OCCUPIED('#', i -> i >= 5);// 4 instead of 5 for part 1

        @Getter
        private final char code;
        private final IntPredicate shouldChange;

        SeatType(char code, IntPredicate shouldChange) {
            this.code = code;
            this.shouldChange = shouldChange;
        }

        public boolean shouldChange(int occupiedNeighbors) {
            return shouldChange.test(occupiedNeighbors);
        }

        public static SeatType of(char code) {
            return Arrays.stream(values())
                         .filter(type -> type.code == code)
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("SeatType " + code + " is not recognised."));
        }
    }

    public enum Direction {
        UP("U", (start, distance) -> Point.of(start.x, start.y + distance)),
        UP_RIGHT("UR", (start, distance) -> Point.of(start.x + distance, start.y + distance)),
        UP_LEFT("UL", (start, distance) -> Point.of(start.x - distance, start.y + distance)),
        DOWN("D", (start, distance) -> Point.of(start.x, start.y - distance)),
        DOWN_RIGHT("DR", (start, distance) -> Point.of(start.x + distance, start.y - distance)),
        DOWN_LEFT("DL", (start, distance) -> Point.of(start.x - distance, start.y - distance)),
        RIGHT("R", (start, distance) -> Point.of(start.x + distance, start.y)),
        LEFT("L", (start, distance) -> Point.of(start.x - distance, start.y));

        private final String code;
        private final BiFunction<Point, Integer, Point> pointComputer;

        Direction(String code, BiFunction<Point, Integer, Point> pointComputer) {
            this.code = code;
            this.pointComputer = pointComputer;
        }

        public static Direction of(String code) {
            return Arrays.stream(values())
                         .filter(direction -> direction.code.equals(code))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Direction " + code + " is not recognised."));
        }

        public List<Point> computeDirection(Point start, int distanceMax) {
            return IntStream.range(1, distanceMax + 1)
                            .mapToObj(distance -> pointComputer.apply(start, distance))
                            .collect(toList());
        }
    }
}
