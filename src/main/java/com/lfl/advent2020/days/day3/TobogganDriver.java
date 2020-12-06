package com.lfl.advent2020.days.day3;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TobogganDriver implements LinesConsumer {
    private final Map<Point, Tile> map = Maps.mutable.empty();
    private int height;
    private int width;
    @Getter
    private BigInteger product;

    @Override
    public void consume(List<String> lines) {
        buildMap(lines);

        product = Lists.mutable.of(getTreeCount(1, 1),
                                   getTreeCount(3, 1),
                                   getTreeCount(5, 1),
                                   getTreeCount(7, 1),
                                   getTreeCount(1, 2))
                               .stream()
                               .map(BigInteger::valueOf)
                               .reduce(BigInteger::multiply)
                               .orElse(BigInteger.ZERO);

        log.info("Tree count = {}", product);
    }

    private int getTreeCount(int widthOffset, int heightOffset) {
        Point spot = Point.ZERO;
        int treeCount = 0;

        while (spot.getY() < height) {
            if (map.get(spot).isTree()) {
                treeCount++;
            }

            int newX = (spot.getX() + widthOffset) % width;
            int newY = spot.getY() + heightOffset;
            spot = Point.of(newX, newY);
        }
        return treeCount;
    }

    private void buildMap(List<String> lines) {
        height = lines.size();
        width = lines.get(0).length();
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) {
                map.put(Point.of(j, i), Tile.of(line.charAt(j)));
            }
        }
    }

    public enum Tile {
        EMPTY('.'),
        TREE('#');

        private final char code;

        Tile(char code) {
            this.code = code;
        }

        public boolean isTree() {
            return this.equals(Tile.TREE);
        }

        public static Tile of(char code) {
            return Arrays.stream(values())
                         .filter(tileId -> tileId.code == code)
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Tile " + code + " is not recognised."));
        }
    }
}
