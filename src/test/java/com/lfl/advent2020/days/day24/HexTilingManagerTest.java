package com.lfl.advent2020.days.day24;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class HexTilingManagerTest {

    @Test
    public void test() {
        //Given
        HexTilingManager service = new HexTilingManager();
        MutableList<String> lines = Lists.mutable.of("sesenwnenenewseeswwswswwnenewsewsw",
                                                     "neeenesenwnwwswnenewnwwsewnenwseswesw",
                                                     "seswneswswsenwwnwse",
                                                     "nwnwneseeswswnenewneswwnewseswneseene",
                                                     "swweswneswnenwsewnwneneseenw",
                                                     "eesenwseswswnenwswnwnwsewwnwsene",
                                                     "sewnenenenesenwsewnenwwwse",
                                                     "wenwwweseeeweswwwnwwe",
                                                     "wsweesenenewnwwnwsenewsenwwsesesenwne",
                                                     "neeswseenwwswnwswswnw",
                                                     "nenwswwsewswnenenewsenwsenwnesesenew",
                                                     "enewnwewneswsewnwswenweswnenwsenwsw",
                                                     "sweneswneswneneenwnewenewwneswswnese",
                                                     "swwesenesewenwneswnwwneseswwne",
                                                     "enesenwswwswneneswsenwnewswseenwsese",
                                                     "wnwnesenesenenwwnenwsewesewsesesew",
                                                     "nenewswnwewswnenesenwnesewesw",
                                                     "eneswnwswnwsenenwnwnwwseeswneewsenese",
                                                     "neswnwewnwnwseenwseesewsenwsweewe",
                                                     "wseweeenwnesenwwwswnew");

        //When
        service.consume(lines);

        //Then
    }

    @Test
    public void test2() {
        //Given
        HexTilingManager service = new HexTilingManager();
        MutableList<String> lines = Lists.mutable.of("esenee");

        //When
        service.consume(lines);

        //Then
    }
}