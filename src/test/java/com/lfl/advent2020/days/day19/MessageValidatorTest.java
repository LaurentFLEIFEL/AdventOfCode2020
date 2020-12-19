package com.lfl.advent2020.days.day19;

import org.assertj.core.api.Assertions;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.Test;

public class MessageValidatorTest {

    @Test
    public void test() {
        //Given
        MessageValidator service = new MessageValidator();
        MutableList<String> lines = Lists.mutable.of("0: 4 1 5",
                                                     "1: 2 3 | 3 2",
                                                     "2: 4 4 | 5 5",
                                                     "3: 4 5 | 5 4",
                                                     "4: \"a\"",
                                                     "5: \"b\"",
                                                     "",
                                                     "ababbb",
                                                     "bababa",
                                                     "abbbab",
                                                     "aaabbb",
                                                     "aaaabbb");
        int expectedCount = 2;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getCount()).isEqualTo(expectedCount);
    }

    @Test
    public void test2() {
        //Given
        MessageValidator service = new MessageValidator();
        MutableList<String> lines = Lists.mutable.of("42: 9 14 | 10 1",
                                                     "9: 14 27 | 1 26",
                                                     "10: 23 14 | 28 1",
                                                     "1: \"a\"",
                                                     "11: 42 31",
                                                     "5: 1 14 | 15 1",
                                                     "19: 14 1 | 14 14",
                                                     "12: 24 14 | 19 1",
                                                     "16: 15 1 | 14 14",
                                                     "31: 14 17 | 1 13",
                                                     "6: 14 14 | 1 14",
                                                     "2: 1 24 | 14 4",
                                                     "0: 8 11",
                                                     "13: 14 3 | 1 12",
                                                     "15: 1 | 14",
                                                     "17: 14 2 | 1 7",
                                                     "23: 25 1 | 22 14",
                                                     "28: 16 1",
                                                     "4: 1 1",
                                                     "20: 14 14 | 1 15",
                                                     "3: 5 14 | 16 1",
                                                     "27: 1 6 | 14 18",
                                                     "14: \"b\"",
                                                     "21: 14 1 | 1 14",
                                                     "25: 1 1 | 1 14",
                                                     "22: 14 14",
                                                     "8: 42",
                                                     "26: 14 22 | 1 20",
                                                     "18: 15 15",
                                                     "7: 14 5 | 1 21",
                                                     "24: 14 1",
                                                     "",
                                                     "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa",
                                                     "bbabbbbaabaabba",
                                                     "babbbbaabbbbbabbbbbbaabaaabaaa",
                                                     "aaabbbbbbaaaabaababaabababbabaaabbababababaaa",
                                                     "bbbbbbbaaaabbbbaaabbabaaa",
                                                     "bbbababbbbaaaaaaaabbababaaababaabab",
                                                     "ababaaaaaabaaab",
                                                     "ababaaaaabbbaba",
                                                     "baabbaaaabbaaaababbaababb",
                                                     "abbbbabbbbaaaababbbbbbaaaababb",
                                                     "aaaaabbaabaaaaababaa",
                                                     "aaaabbaaaabbaaa",
                                                     "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa",
                                                     "babaaabbbaaabaababbaabababaaab",
                                                     "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba");
        int expectedCount = 3;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getCount()).isEqualTo(expectedCount);
    }

    @Test
    public void test3() {
        //Given
        MessageValidator service = new MessageValidator();
        MutableList<String> lines = Lists.mutable.of("42: 9 14 | 10 1",
                                                     "9: 14 27 | 1 26",
                                                     "10: 23 14 | 28 1",
                                                     "1: \"a\"",
                                                     "11: 42 31 | 42 11 31",
                                                     "5: 1 14 | 15 1",
                                                     "19: 14 1 | 14 14",
                                                     "12: 24 14 | 19 1",
                                                     "16: 15 1 | 14 14",
                                                     "31: 14 17 | 1 13",
                                                     "6: 14 14 | 1 14",
                                                     "2: 1 24 | 14 4",
                                                     "0: 8 11",
                                                     "13: 14 3 | 1 12",
                                                     "15: 1 | 14",
                                                     "17: 14 2 | 1 7",
                                                     "23: 25 1 | 22 14",
                                                     "28: 16 1",
                                                     "4: 1 1",
                                                     "20: 14 14 | 1 15",
                                                     "3: 5 14 | 16 1",
                                                     "27: 1 6 | 14 18",
                                                     "14: \"b\"",
                                                     "21: 14 1 | 1 14",
                                                     "25: 1 1 | 1 14",
                                                     "22: 14 14",
                                                     "8: 42 | 42 8",
                                                     "26: 14 22 | 1 20",
                                                     "18: 15 15",
                                                     "7: 14 5 | 1 21",
                                                     "24: 14 1",
                                                     "",
                                                     "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa",
                                                     "bbabbbbaabaabba",
                                                     "babbbbaabbbbbabbbbbbaabaaabaaa",
                                                     "aaabbbbbbaaaabaababaabababbabaaabbababababaaa",
                                                     "bbbbbbbaaaabbbbaaabbabaaa",
                                                     "bbbababbbbaaaaaaaabbababaaababaabab",
                                                     "ababaaaaaabaaab",
                                                     "ababaaaaabbbaba",
                                                     "baabbaaaabbaaaababbaababb",
                                                     "abbbbabbbbaaaababbbbbbaaaababb",
                                                     "aaaaabbaabaaaaababaa",
                                                     "aaaabbaaaabbaaa",
                                                     "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa",
                                                     "babaaabbbaaabaababbaabababaaab",
                                                     "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba");
        int expectedCount = 12;

        //When
        service.consume(lines);

        //Then
        Assertions.assertThat(service.getCount()).isEqualTo(expectedCount);
    }

    @Test
    public void finalRule() {
        //Given
        String line = "131: \"a\"";
        String expectedLine = "131: \"a\"";

        //When
        FinalRule rule = FinalRule.of(line);

        //Then
        Assertions.assertThat(rule.toString()).isEqualTo(expectedLine);
    }

    @Test
    public void chainedRule() {
        //Given
        String line = "23: 40 130";
        String expectedLine = "23: 40 130";

        //When
        ChainedRule rule = ChainedRule.of(line, null);

        //Then
        Assertions.assertThat(rule.toString()).isEqualTo(expectedLine);
    }

    @Test
    public void chainedRule2() {
        //Given
        String line = "23: 40 130 54";
        String expectedLine = "23: 40 130 54";

        //When
        ChainedRule rule = ChainedRule.of(line, null);

        //Then
        Assertions.assertThat(rule.toString()).isEqualTo(expectedLine);
    }

    @Test
    public void orChainedRule() {
        //Given
        String line = "54: 105 116 | 104 131";
        String expectedLine = "54: -1: 105 116 | -2: 104 131";

        //When
        OrChainedRule rule = OrChainedRule.of(line, null);

        //Then
        Assertions.assertThat(rule.toString()).isEqualTo(expectedLine);
    }

    @Test
    public void orChainedRule2() {
        //Given
        String line = "130: 131 | 116";
        String expectedLine = "130: -1: 131 | -2: 116";

        //When
        OrChainedRule rule = OrChainedRule.of(line, null);

        //Then
        Assertions.assertThat(rule.toString()).isEqualTo(expectedLine);
    }
}