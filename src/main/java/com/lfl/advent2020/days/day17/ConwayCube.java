package com.lfl.advent2020.days.day17;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point4;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.map.MutableMap;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.LongPredicate;

@Slf4j
@Service
public class ConwayCube implements LinesConsumer {

    private MutableMap<Point4, State> map = Maps.mutable.empty();
    private static final int CYCLE_MAX = 6;

    @Override
    public void consume(List<String> lines) {
        buildMap(lines);

        for (int cycle = 0; cycle < CYCLE_MAX; cycle++) {
            addNeighbors();
            updateStates();
        }

        int size = map.select(State::isActive)
                      .size();

        log.info("Number of active cube = {}", size);
    }

    private void updateStates() {
        MutableMap<Point4, State> tmp = Maps.mutable.withInitialCapacity(map.size());
        map.forEachKeyValue((point, state) -> {
            long count = findNeighbors(point).stream()
                                             .filter(neighbor -> map.containsKey(neighbor))
                                             .map(neighbor -> map.get(neighbor))
                                             .filter(State::isActive)
                                             .count();

            tmp.put(point, state.shouldBeActive(count) ? State.ACTIVE : State.EMPTY);
        });

        map = tmp;
    }

    private void addNeighbors() {
        MutableMap<Point4, State> tmp = Maps.mutable.withInitialCapacity(map.size());

        map.forEachKeyValue((point, state) -> {
            tmp.put(point, state);
            findNeighbors(point).forEach(neighbor -> tmp.putIfAbsent(neighbor, State.EMPTY));
        });

        map = tmp;
    }


    private final MutableMap<Point4, Set<Point4>> memoizedNeighbors = Maps.mutable.empty();

    private Set<Point4> findNeighbors(Point4 start) {
        return memoizedNeighbors.computeIfAbsent(start, this::doFindNeighbors);
    }

    private Set<Point4> doFindNeighbors(Point4 start) {
        int x = start.x;
        int y = start.y;
        int z = start.z;
        int t = start.t;

        Set<Point4> result = Sets.mutable.empty();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dt = -1; dt <= 1; dt++) {
                        if (dx == 0 && dy == 0 && dz == 0 && dt == 0) {
                            continue;
                        }
                        result.add(Point4.of(x + dx, y + dy, z + dz, t + dt));
                    }
                }
            }
        }

        return result;
    }

    private void buildMap(List<String> lines) {
        int height = lines.size();
        int width = lines.get(0).length();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                State state = State.of(lines.get(i).charAt(j));
                map.put(Point4.of(i, j, 0, 0), state);
            }
        }
    }

    enum State {
        EMPTY('.', activeNeighbors -> activeNeighbors == 3),
        ACTIVE('#', activeNeighbors -> activeNeighbors == 2 || activeNeighbors == 3);

        private final char code;
        private final LongPredicate shouldBeActive;

        State(char code, LongPredicate shouldBeActive) {
            this.code = code;
            this.shouldBeActive = shouldBeActive;
        }

        public boolean shouldBeActive(long active) {
            return shouldBeActive.test(active);
        }

        public boolean isActive() {
            return this.equals(ACTIVE);
        }

        public static State of(char code) {
            return Arrays.stream(values())
                         .filter(state -> state.code == code)
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("State " + code + " is not recognised."));
        }
    }
}
