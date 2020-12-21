package com.lfl.advent2020.days.day20;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImageComputer implements LinesConsumer {

    private int max;

    @Override
    public void consume(List<String> lines) {
        Set<Tile> tiles = parseInput(lines);

        BigInteger product = tiles.stream()
                                  .filter(tile -> tile.nbOfMatchingEdges(tiles) == 2)
                                  .map(tile -> BigInteger.valueOf(tile.getId()))
                                  .reduce(BigInteger::multiply)
                                  .orElse(BigInteger.ZERO);

        log.info("product = {}", product);
        max = (int) Math.sqrt(tiles.size());

        MutableMap<Point, Tile> image = buildImage(tiles);

        reorder(image);

        MutableMap<Point, Pixel> picture = extractPicture(image);

        int max2 = picture.keySet().stream().mapToInt(point -> point.x).max().getAsInt();
        for (int i = 0; i < max2 + 1; i++) {
            for (int j = 0; j < max2 + 1; j++) {
                System.out.print(picture.get(Point.of(i, j)).getCode());
            }
            System.out.println();
        }

        Set<Nessie> nessies = Sets.mutable.of(Nessie.of(0),
                                              Nessie.of(1).rotate(),
                                              Nessie.of(2).rotate().rotate(),
                                              Nessie.of(3).rotate().rotate().rotate(),
                                              Nessie.of(4).flip(),
                                              Nessie.of(5).flip().rotate(),
                                              Nessie.of(6).flip().rotate().rotate(),
                                              Nessie.of(7).flip().rotate().rotate().rotate());

        long sumNessie = nessies.stream()
                                .mapToLong(nessie -> countNessie(nessie, picture))
                                .sum();

        System.out.println("sumNessie = " + sumNessie);

        long activePixel = picture.values()
                                  .stream()
                                  .filter(Pixel::isActive)
                                  .count();

        log.info("Result = {}", activePixel - sumNessie * 15);

    }

    private long countNessie(Nessie nessie, MutableMap<Point, Pixel> picture) {
        return picture.keySet()
                      .stream()
                      .filter(point -> hasNessie(point, nessie, picture))
                      .count();
    }

    private boolean hasNessie(Point point, Nessie nessie, MutableMap<Point, Pixel> picture) {
        return nessie.getTile().keySet()
                     .stream()
                     .map(nessiePoint -> Point.of(point.x + nessiePoint.x, point.y + nessiePoint.y))
                     .allMatch(point1 -> picture.getIfAbsent(point1, () -> Pixel.EMPTY).isActive());
    }

    private MutableMap<Point, Pixel> extractPicture(MutableMap<Point, Tile> image) {
        List<List<Pixel>> picture = Lists.mutable.empty();

        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                MutableList<MutableList<Pixel>> tile = image.get(Point.of(x, y))
                                                            .extractPicture();
                for (int j = 0; j < tile.size(); j++) {
                    for (int i = 0; i < tile.get(0).size(); i++) {
                        if (picture.size() <= x * tile.size() + j) {
                            picture.add(Lists.mutable.empty());
                        }
                        picture.get(x * tile.size() + j).add(tile.get(j).get(i));
                    }
                }

            }
        }

        MutableMap<Point, Pixel> result = Maps.mutable.empty();

        for (int i = 0; i < picture.size(); i++) {
            for (int j = 0; j < picture.get(0).size(); j++) {
                result.put(Point.of(i, j), picture.get(i).get(j));
            }
        }

        return result;
    }

    private void reorder(MutableMap<Point, Tile> image) {
        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                Tile tile = image.get(Point.of(x, y));
                if (x == 0 && y == 0) {
                    tile.cornerOrder(image.get(Point.of(0, 1)), image.get(Point.of(1, 0)));
                    continue;
                }

                if (x == 0) {
                    tile.order(Tile.Direction.LEFT, image.get(Point.of(0, y - 1)));
                    continue;
                }
                tile.order(Tile.Direction.UP, image.get(Point.of(x - 1, y)));
            }
        }
    }

    private MutableMap<Point, Tile> buildImage(Set<Tile> tiles) {
        Map<Integer, List<Tile>> tileByEgdes = tiles.stream()
                                                    .collect(Collectors.groupingBy(
                                                            tile -> tile.nbOfMatchingEdges(tiles)));

        MutableMap<Point, Tile> image = Maps.mutable.empty();
        Set<Tile> detected = Sets.mutable.empty();

        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                if (x == 0 && y == 0) {
                    image.put(Point.ZERO, tileByEgdes.get(2).get(0));
                    detected.add(tileByEgdes.get(2).get(0));
                    continue;
                }

                Tile foundTile;

                if (x == 0) {
                    Tile previous = image.get(Point.of(0, y - 1));
                    foundTile = findNextTile(tiles, detected, previous);
                } else if (y == 0) {
                    Tile previous = image.get(Point.of(x - 1, 0));
                    foundTile = findNextTile(tiles, detected, previous);
                } else if (y == max - 1) {
                    Tile previous = image.get(Point.of(x - 1, max - 1));
                    foundTile = findNextTile(tiles, detected, previous);
                } else if (x == max - 1) {
                    Tile previous = image.get(Point.of(max - 1, y - 1));
                    foundTile = findNextTile(tiles, detected, previous);
                } else {

                    Tile previous = image.get(Point.of(x, y - 1));
                    foundTile = tiles.stream()
                                     .filter(tile -> !detected.contains(tile))
                                     .filter(tile -> tile.nbOfMatchingEdges(detected) == 2 && tile.matchTile(previous))
                                     .findAny()
                                     .orElseThrow(() -> new IllegalStateException("No Tile found"));
                }
                image.put(Point.of(x, y), foundTile);
                detected.add(foundTile);
            }
        }

        return image;
    }

    private Tile findNextTile(Set<Tile> tiles, Set<Tile> detected, Tile previous) {
        return tiles.stream()
                    .filter(tile -> !detected.contains(tile))
                    .filter(tile -> tile.nbOfMatchingEdges(tiles) < 4 && tile.matchTile(previous))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("No Tile found"));
    }

    private Set<Tile> parseInput(List<String> lines) {
        List<String> linesForTile = Lists.mutable.empty();
        Set<Tile> tiles = Sets.mutable.empty();
        for (String line : lines) {
            if (line.isEmpty()) {
                tiles.add(Tile.of(linesForTile));
                linesForTile.clear();
                continue;
            }

            linesForTile.add(line);
        }

        if (!linesForTile.isEmpty()) {
            tiles.add(Tile.of(linesForTile));
        }
        return tiles;
    }


}
