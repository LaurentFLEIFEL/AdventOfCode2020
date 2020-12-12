package com.lfl.advent2020.days.day12;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.primitive.ImmutableIntIntMap;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
public class FerryNavigator implements LinesConsumer {

    @Getter
    private Point finalPosition;

    @Override
    public void consume(List<String> lines) {
        Ferry ferry = Ferry.of(Point.ZERO, Point.of(10, 1));
        for (String line : lines) {
            Action action = Action.valueOf(line.substring(0, 1));
            int value = Integer.parseInt(line.substring(1));
            ferry.executeAction(action, value);
        }

        finalPosition = ferry.getPosition();
        log.info("Final position = {}", finalPosition);
        log.info("Manhattan distance = {}", finalPosition.module1());
    }

    @Getter
    @Setter
    public static class Ferry {
        private Point position;
        private Point wayPoint;//relative

        public static Ferry of(Point position, Point wayPoint) {
            Ferry ferry = new Ferry();
            ferry.setPosition(position);
            ferry.setWayPoint(wayPoint);
            return ferry;
        }

        public void executeAction(Action action, int value) {
            //log.info("Action = {}, value = {}", action, value);
            wayPoint = action.computeWayPoint(this, value);
            position = action.computePosition(this, value);
            //log.info("Waypoint = {}", wayPoint);
            //log.info("Position = {}", position);
        }
    }

    @SuppressWarnings("unused")
    public enum Action {
        N((ferry, i) -> Point.of(ferry.getWayPoint().x, ferry.getWayPoint().y + i),
          (ferry, i) -> ferry.getPosition()),
        S((ferry, i) -> Point.of(ferry.getWayPoint().x, ferry.getWayPoint().y - i),
          (ferry, i) -> ferry.getPosition()),
        E((ferry, i) -> Point.of(ferry.getWayPoint().x + i, ferry.getWayPoint().y),
          (ferry, i) -> ferry.getPosition()),
        W((ferry, i) -> Point.of(ferry.getWayPoint().x - i, ferry.getWayPoint().y),
          (ferry, i) -> ferry.getPosition()),
        L((ferry, i) -> applyNTime(ferry.getWayPoint(), getOrdinal(i), FerryNavigator::turnLeft),
          (ferry, i) -> ferry.getPosition()),
        R((ferry, i) -> applyNTime(ferry.getWayPoint(), getOrdinal(i), FerryNavigator::turnRight),
          (ferry, i) -> ferry.getPosition()),
        F((ferry, i) -> ferry.getWayPoint(),
          (ferry, i) -> Point.of(ferry.getPosition().x + i * ferry.getWayPoint().x, ferry.getPosition().y + i * ferry.getWayPoint().y));

        private final BiFunction<Ferry, Integer, Point> wayPointComputer;
        private final BiFunction<Ferry, Integer, Point> positionComputer;

        Action(BiFunction<Ferry, Integer, Point> wayPointComputer, BiFunction<Ferry, Integer, Point> positionComputer) {
            this.wayPointComputer = wayPointComputer;
            this.positionComputer = positionComputer;
        }

        public Point computeWayPoint(Ferry ferry, int value) {
            return wayPointComputer.apply(ferry, value);
        }

        public Point computePosition(Ferry ferry, int value) {
            return positionComputer.apply(ferry, value);
        }
    }

    private static Point applyNTime(Point start, int times, Function<Point, Point> toApply) {
        while (times > 0) {
            start = toApply.apply(start);
            times--;
        }
        return start;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static Point turnRight(Point start) {
        return Point.of(start.y, -start.x);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static Point turnLeft(Point start) {
        return Point.of(-start.y, start.x);
    }

    private static final ImmutableIntIntMap degreeToOrdinal = IntIntMaps.mutable.empty()
                                                                                .withKeyValue(90, 1)
                                                                                .withKeyValue(180, 2)
                                                                                .withKeyValue(270, 3)
                                                                                .toImmutable();

    private static int getOrdinal(int value) {
        return degreeToOrdinal.get(value);
    }
}
