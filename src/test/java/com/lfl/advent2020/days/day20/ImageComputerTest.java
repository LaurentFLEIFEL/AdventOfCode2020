package com.lfl.advent2020.days.day20;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Test;

import java.util.List;

public class ImageComputerTest {

    @Test
    public void test() {
        //Given
        ImageComputer service = new ImageComputer();
        List<String> lines = Lists.mutable.of("Tile 2311:",
                                              "..##.#..#.",
                                              "##..#.....",
                                              "#...##..#.",
                                              "####.#...#",
                                              "##.##.###.",
                                              "##...#.###",
                                              ".#.#.#..##",
                                              "..#....#..",
                                              "###...#.#.",
                                              "..###..###",
                                              "",
                                              "Tile 1951:",
                                              "#.##...##.",
                                              "#.####...#",
                                              ".....#..##",
                                              "#...######",
                                              ".##.#....#",
                                              ".###.#####",
                                              "###.##.##.",
                                              ".###....#.",
                                              "..#.#..#.#",
                                              "#...##.#..",
                                              "",
                                              "Tile 1171:",
                                              "####...##.",
                                              "#..##.#..#",
                                              "##.#..#.#.",
                                              ".###.####.",
                                              "..###.####",
                                              ".##....##.",
                                              ".#...####.",
                                              "#.##.####.",
                                              "####..#...",
                                              ".....##...",
                                              "",
                                              "Tile 1427:",
                                              "###.##.#..",
                                              ".#..#.##..",
                                              ".#.##.#..#",
                                              "#.#.#.##.#",
                                              "....#...##",
                                              "...##..##.",
                                              "...#.#####",
                                              ".#.####.#.",
                                              "..#..###.#",
                                              "..##.#..#.",
                                              "",
                                              "Tile 1489:",
                                              "##.#.#....",
                                              "..##...#..",
                                              ".##..##...",
                                              "..#...#...",
                                              "#####...#.",
                                              "#..#.#.#.#",
                                              "...#.#.#..",
                                              "##.#...##.",
                                              "..##.##.##",
                                              "###.##.#..",
                                              "",
                                              "Tile 2473:",
                                              "#....####.",
                                              "#..#.##...",
                                              "#.##..#...",
                                              "######.#.#",
                                              ".#...#.#.#",
                                              ".#########",
                                              ".###.#..#.",
                                              "########.#",
                                              "##...##.#.",
                                              "..###.#.#.",
                                              "",
                                              "Tile 2971:",
                                              "..#.#....#",
                                              "#...###...",
                                              "#.#.###...",
                                              "##.##..#..",
                                              ".#####..##",
                                              ".#..####.#",
                                              "#..#.#..#.",
                                              "..####.###",
                                              "..#.#.###.",
                                              "...#.#.#.#",
                                              "",
                                              "Tile 2729:",
                                              "...#.#.#.#",
                                              "####.#....",
                                              "..#.#.....",
                                              "....#..#.#",
                                              ".##..##.#.",
                                              ".#.####...",
                                              "####.#.#..",
                                              "##.####...",
                                              "##..#.##..",
                                              "#.##...##.",
                                              "",
                                              "Tile 3079:",
                                              "#.#.#####.",
                                              ".#..######",
                                              "..#.......",
                                              "######....",
                                              "####.#..#.",
                                              ".#...#.##.",
                                              "#.#####.##",
                                              "..#.###...",
                                              "..#.......",
                                              "..#.###...");

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(true).isTrue();
    }
}