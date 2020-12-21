package com.lfl.advent2020.days.day20;

import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tile {
    private static final int TILE_HEIGHT = 10;
    private static final int TILE_WIDTH = 10;
    private static final Pattern tileIdPattern = Pattern.compile("Tile (?<id>\\d+):");

    @Getter
    private int id;
    private Map<Point, Pixel> tile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Tile of(List<String> lines) {
        Matcher matcher = tileIdPattern.matcher(lines.get(0));
        matcher.find();
        int id = Integer.parseInt(matcher.group("id"));

        Map<Point, Pixel> tile = Maps.mutable.empty();
        for (int y = 1; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(1).length(); x++) {
                tile.put(Point.of(x, y - 1), Pixel.of(lines.get(y).charAt(x)));
            }
        }

        Tile result = new Tile();
        result.id = id;
        result.tile = tile;
        return result;
    }

    public MutableList<MutableList<Pixel>> extractPicture() {
        MutableList<MutableList<Pixel>> picture = Lists.mutable.empty();
        for (int y = 1; y < TILE_HEIGHT - 1; y++) {
            MutableList<Pixel> line = Lists.mutable.empty();
            for (int x = 1; x < TILE_WIDTH - 1; x++) {
                line.add(tile.get(Point.of(x, y)));
            }
            picture.add(line);
        }

        return picture;
    }

    public int nbOfMatchingEdges(Set<Tile> tiles) {
        Set<MutableList<Pixel>> myEdges = edges();

        int count = 0;
        for (MutableList<Pixel> myEdge : myEdges) {
            boolean match = tiles.stream()
                                 .filter(tile -> tile.id != this.id)
                                 .anyMatch(tile -> tile.edges().contains(myEdge));
            if (match) {
                count++;
                continue;
            }

            MutableList<Pixel> reverse = myEdge.reverseThis();
            boolean matchReversed = tiles.stream()
                                         .filter(tile -> tile.id != this.id)
                                         .anyMatch(tile -> tile.edges().contains(reverse));
            if (matchReversed)
                count++;
        }

        return count;
    }

    public boolean matchTile(Tile other) {
        Set<MutableList<Pixel>> myEdges = edges();
        for (MutableList<Pixel> myEdge : myEdges) {
            if (matchEdge(other, myEdge))
                return true;
        }

        return false;
    }

    private boolean matchEdge(Tile other, MutableList<Pixel> myEdge) {
        boolean match = other.edges().contains(myEdge);
        if (match) {
            return true;
        }

        MutableList<Pixel> reverse = myEdge.reverseThis();
        return other.edges().contains(reverse);
    }

    public void cornerOrder(Tile right, Tile down) {
        Map<Direction, MutableList<Pixel>> edges = edgesByDirection();

        int count = 0;
        while (!(matchEdge(right, edges.get(Direction.RIGHT)) && matchEdge(down, edges.get(Direction.DOWN)))) {
            if (count > 4) {
                flip();
                count = 0;
            }
            rotate();
            edges = edgesByDirection();
            count++;
        }
    }

    public void order(Direction direction, Tile other) {
        Map<Direction, MutableList<Pixel>> edges = edgesByDirection();

        while (!(matchEdge(other, edges.get(direction)))) {
            rotate();
            edges = edgesByDirection();
        }

        boolean match = other.edges().contains(edges.get(direction));
        if (!match) {
            flip();
        }

    }

    private void rotate() {
        Map<Point, Pixel> newTile = Maps.mutable.withInitialCapacity(tile.size());
        for (int i = 0; i < TILE_WIDTH; i++) {
            for (int j = 0; j < TILE_HEIGHT; j++) {
                newTile.put(Point.of(i, j), tile.get(Point.of(TILE_WIDTH - 1 - j, i)));
            }
        }

        tile = newTile;
    }

    private void flip() {
        Map<Point, Pixel> newTile = Maps.mutable.withInitialCapacity(tile.size());
        for (int i = 0; i < TILE_WIDTH; i++) {
            for (int j = 0; j < TILE_HEIGHT; j++) {
                newTile.put(Point.of(i, j), tile.get(Point.of(i, TILE_HEIGHT - 1 - j)));
            }
        }

        tile = newTile;
    }

    public Set<MutableList<Pixel>> edges() {
        MutableList<Pixel> edgeRight = Lists.mutable.empty();
        MutableList<Pixel> edgeLeft = Lists.mutable.empty();
        for (int i = 0; i < TILE_HEIGHT; i++) {
            edgeLeft.add(tile.get(Point.of(0, i)));
            edgeRight.add(tile.get(Point.of(9, i)));
        }

        MutableList<Pixel> edgeUp = Lists.mutable.empty();
        MutableList<Pixel> edgeDown = Lists.mutable.empty();
        for (int i = 0; i < TILE_WIDTH; i++) {
            edgeUp.add(tile.get(Point.of(i, 0)));
            edgeDown.add(tile.get(Point.of(i, 9)));
        }

        return Sets.mutable.<MutableList<Pixel>>empty()
                .with(edgeLeft)
                .with(edgeUp)
                .with(edgeRight)
                .with(edgeDown);
    }

    public Map<Direction, MutableList<Pixel>> edgesByDirection() {
        MutableList<Pixel> edgeRight = Lists.mutable.empty();
        MutableList<Pixel> edgeLeft = Lists.mutable.empty();
        for (int i = 0; i < TILE_HEIGHT; i++) {
            edgeLeft.add(tile.get(Point.of(0, i)));
            edgeRight.add(tile.get(Point.of(9, i)));
        }

        MutableList<Pixel> edgeUp = Lists.mutable.empty();
        MutableList<Pixel> edgeDown = Lists.mutable.empty();
        for (int i = 0; i < TILE_WIDTH; i++) {
            edgeUp.add(tile.get(Point.of(i, 0)));
            edgeDown.add(tile.get(Point.of(i, 9)));
        }

        return Maps.mutable.<Direction, MutableList<Pixel>>empty()
                .withKeyValue(Direction.UP, edgeUp)
                .withKeyValue(Direction.RIGHT, edgeRight)
                .withKeyValue(Direction.DOWN, edgeDown)
                .withKeyValue(Direction.LEFT, edgeLeft);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tile tile = (Tile) o;
        return id == tile.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tile{" +
               "id=" + id +
               '}';
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;
    }
}