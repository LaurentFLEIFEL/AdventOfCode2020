package com.lfl.advent2020.days.day2;

import com.lfl.advent2020.LinesConsumer;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PasswordAnalyzer implements LinesConsumer {

    private final static Pattern INPUT_PATTERN = Pattern.compile("(\\d+)-(\\d+) (\\S): (\\w+)");

    @Override
    public void consume(List<String> lines) {
        long count = lines.stream()
                          .map(PasswordInput::fromInput)
                          .filter(Objects::nonNull)
                          .filter(PasswordInput::isConsistent2)
                          .count();

        log.info("count = {}", count);
    }

    @ToString
    private static class PasswordInput {
        private final int lowerBound;
        private final int upperBound;
        private final String character;
        private final String password;

        public PasswordInput(int lowerBound, int upperBound, String character, String password) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.character = character;
            this.password = password;
        }

        public static PasswordInput of(int lowerBound, int upperBound, String character, String password) {
            return new PasswordInput(lowerBound, upperBound, character, password);
        }

        public static PasswordInput fromInput(String line) {
            Matcher matcher = INPUT_PATTERN.matcher(line);
            if (matcher.find()) {
                return PasswordInput.of(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), matcher.group(3), matcher.group(4));
            }

            log.error("Error while parsing line = {}", line);
            return null;
        }

        //for part 1
        @SuppressWarnings("unused")
        public boolean isConsistent1() {
            char c1 = character.charAt(0);
            long count = password.chars()
                                 .filter(c -> c == c1)
                                 .count();

            return lowerBound <= count && upperBound >= count;
        }

        public boolean isConsistent2() {
            char c = character.charAt(0);
            char c1 = password.charAt(lowerBound - 1);
            char c2 = password.charAt(upperBound - 1);

            return (c == c1 && c != c2) || (c != c1 && c == c2);
        }
    }
}
