package com.lfl.advent2020.days.day4;

import com.lfl.advent2020.LinesConsumer;
import com.lfl.advent2020.utils.Strings;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
public class PassportValidator implements LinesConsumer {

    private static final ImmutableSet<String> EYE_COLORS = Sets.immutable.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    @Getter
    private long validPassportCount;

    @Override
    public void consume(List<String> lines) {
        List<Passport> passports = parsePassports(lines);

        validPassportCount = passports.stream()
                                      .filter(Passport::isValid)
                                      .count();

        log.info("Number of valid passport = {}", validPassportCount);
    }

    private List<Passport> parsePassports(List<String> lines) {
        List<Passport> passports = Lists.mutable.empty();
        List<String> partialPassport = Lists.mutable.empty();
        for (String line : lines) {
            if ("".equals(line)) {
                passports.add(Passport.of(partialPassport));
                partialPassport = Lists.mutable.empty();
                continue;
            }

            partialPassport.add(line);
        }

        if (!partialPassport.isEmpty()) {
            passports.add(Passport.of(partialPassport));
        }
        return passports;
    }

    @Data
    public static class Passport {
        private Integer birthYear;
        private Integer issueYear;
        private Integer expirationYear;
        private String height;
        private String hairColor;
        private String eyeColor;
        private String passportId;
        private Integer countryId;

        public static Passport of(List<String> lines) {
            return Passport.of(String.join(" ", lines));
        }

        public static Passport of(String line) {
            Passport passport = new Passport();

            String[] fields = line.split(" ", -1);
            for (String field : fields) {
                String[] fieldAttributes = field.split(":");
                PassportField.of(fieldAttributes[0]).setField(passport, fieldAttributes[1]);
            }

            return passport;
        }

        public boolean isValid() {
            return Arrays.stream(PassportField.values())
                         .allMatch(field -> field.isValid(this));
        }
    }

    public enum PassportField {
        BIRTH_YEAR("byr",
                   false,
                   (p, s) -> p.setBirthYear(Integer.parseInt(s)),
                   Passport::getBirthYear,
                   p -> p.getBirthYear() >= 1920
                        && p.getBirthYear() <= 2002),
        ISSUE_YEAR("iyr",
                   false,
                   (p, s) -> p.setIssueYear(Integer.parseInt(s)),
                   Passport::getIssueYear,
                   p -> p.getIssueYear() >= 2010
                        && p.getIssueYear() <= 2020),
        EXPIRATION_YEAR("eyr",
                        false,
                        (p, s) -> p.setExpirationYear(Integer.parseInt(s)),
                        Passport::getExpirationYear,
                        p -> p.getExpirationYear() >= 2020
                             && p.getExpirationYear() <= 2030),
        HEIGHT("hgt",
               false,
               Passport::setHeight,
               Passport::getHeight,
               p -> HeightUnit.validHeight(p.getHeight())),
        HAIR_COLOR("hcl",
                   false,
                   Passport::setHairColor,
                   Passport::getHairColor,
                   p -> p.getHairColor().length() == 7
                        && p.getHairColor().charAt(0) == '#'
                        && Strings.isHexadecimalNumeric(p.getHairColor().substring(1))),
        EYE_COLOR("ecl",
                  false,
                  Passport::setEyeColor,
                  Passport::getEyeColor,
                  p -> EYE_COLORS.contains(p.getEyeColor())),
        PASSPORT_ID("pid",
                    false,
                    Passport::setPassportId,
                    Passport::getPassportId,
                    p -> p.getPassportId().length() == 9
                         && Strings.isNumeric(p.getPassportId())),
        COUNTRY_ID("cid",
                   true,
                   (p, s) -> p.setCountryId(Integer.parseInt(s)),
                   Passport::getCountryId,
                   p -> true);

        private final String code;
        private final boolean isOptional;
        private final BiConsumer<Passport, String> fieldSetter;
        private final Function<Passport, Object> fieldGetter;
        private final Predicate<Passport> fieldValidator;

        PassportField(String code,
                      boolean isOptional,
                      BiConsumer<Passport, String> fieldSetter,
                      Function<Passport, Object> fieldGetter,
                      Predicate<Passport> fieldValidator) {
            this.code = code;
            this.isOptional = isOptional;
            this.fieldSetter = fieldSetter;
            this.fieldGetter = fieldGetter;
            this.fieldValidator = fieldValidator;
        }

        public void setField(Passport passport, String value) {
            fieldSetter.accept(passport, value);
        }

        public Object getField(Passport passport) {
            return fieldGetter.apply(passport);
        }

        public boolean isValid(Passport passport) {
            if (isOptional) {
                return true;
            }

            if (Objects.isNull(getField(passport))) {
                return false;
            }

            return fieldValidator.test(passport);
        }

        public static PassportField of(String code) {
            return Arrays.stream(values())
                         .filter(field -> field.code.equals(code))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("PassportField " + code + " is not recognised."));
        }
    }

    public enum HeightUnit {
        CM("cm", 150, 193),
        IN("in", 59, 76);

        private final String code;
        private final int min;
        private final int max;


        HeightUnit(String code, int min, int max) {
            this.code = code;
            this.min = min;
            this.max = max;
        }

        public boolean isValid(int height) {
            return height >= min && height <= max;
        }

        public static boolean validHeight(String height) {
            String unit = height.substring(height.length() - 2);

            HeightUnit heightUnit = HeightUnit.of(unit);
            if (Objects.isNull(heightUnit)) {
                return false;
            }

            int heightValue = Integer.parseInt(height.substring(0, height.length() - 2));
            return heightUnit.isValid(heightValue);
        }

        public static HeightUnit of(String code) {
            return Arrays.stream(values())
                         .filter(field -> field.code.equals(code))
                         .findAny()
                         .orElse(null);
        }
    }
}
