package com.lfl.advent2020.days.day20;

import com.lfl.advent2020.utils.Point;
import lombok.Getter;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class Nessie {

    private static final int NESSIE_WIDTH = 20;
    private static final int NESSIE_HEIGHT = 3;
    private static final List<String> NESSIE = Lists.mutable.of("                  # ",
                                                                "#    ##    ##    ###",
                                                                " #  #  #  #  #  #   ");

    private int id;
    private Map<Point, Pixel> tile;

    public static Nessie of(int id) {
        Nessie nessie = new Nessie();
        Map<Point, Pixel> tile = Maps.mutable.empty();
        for (int y = 1; y < NESSIE.size(); y++) {
            for (int x = 0; x < NESSIE.get(1).length(); x++) {
                if (NESSIE.get(y).charAt(x) == '#')
                    tile.put(Point.of(x, y - 1), Pixel.of(NESSIE.get(y).charAt(x)));
            }
        }

        nessie.id = id;
        nessie.tile = tile;
        return nessie;
    }

    public Nessie rotate() {
        Map<Point, Pixel> newTile = Maps.mutable.withInitialCapacity(tile.size());
        for (int i = 0; i < NESSIE_WIDTH; i++) {
            for (int j = 0; j < NESSIE_HEIGHT; j++) {
                newTile.put(Point.of(i, j), tile.get(Point.of(NESSIE_WIDTH - 1 - j, i)));
            }
        }

        tile = newTile;
        return this;
    }

    public Nessie flip() {
        Map<Point, Pixel> newTile = Maps.mutable.withInitialCapacity(tile.size());
        for (int i = 0; i < NESSIE_WIDTH; i++) {
            for (int j = 0; j < NESSIE_HEIGHT; j++) {
                newTile.put(Point.of(i, j), tile.get(Point.of(i, NESSIE_WIDTH - 1 - j)));
            }
        }

        tile = newTile;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Nessie nessie = (Nessie) o;
        return id == nessie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Nessie{" +
               "id=" + id +
               '}';
    }

}
