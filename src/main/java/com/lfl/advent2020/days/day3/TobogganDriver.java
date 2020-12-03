package com.lfl.advent2020.days.day3;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

        int treeCount11 = getTreeCount(1, 1);
        int treeCount31 = getTreeCount(3, 1);
        int treeCount51 = getTreeCount(5, 1);
        int treeCount71 = getTreeCount(7, 1);
        int treeCount12 = getTreeCount(1, 2);

        product = BigInteger.valueOf(treeCount11)
                            .multiply(BigInteger.valueOf(treeCount31))
                            .multiply(BigInteger.valueOf(treeCount51))
                            .multiply(BigInteger.valueOf(treeCount71))
                            .multiply(BigInteger.valueOf(treeCount12));

        log.info("Tree count = {}", product);
    }

    private int getTreeCount(int widthOffset, int heightOffset) {
        Point spot = Point.ZERO;
        int treeCount = 0;

        if (map.get(spot).equals(Tile.TREE)) {
            treeCount++;
        }

        while (spot.getY() < height - 1) {
            spot = Point.of((spot.getX() + widthOffset) % width, spot.getY() + heightOffset);
            if (map.get(spot).equals(Tile.TREE)) {
                treeCount++;
            }
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

        public static Tile of(char code) {
            return Arrays.stream(values())
                         .filter(tileId -> tileId.code == code)
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Tile " + code + " is not recognised."));
        }
    }
}
