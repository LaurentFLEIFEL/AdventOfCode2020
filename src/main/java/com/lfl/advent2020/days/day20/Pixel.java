package com.lfl.advent2020.days.day20;

import lombok.Getter;

import java.util.Arrays;

public enum Pixel {
    EMPTY('.'),
    ACTIVE('#');

    @Getter
    private final char code;

    Pixel(char code) {
        this.code = code;
    }

    public boolean isActive() {
        return this.equals(Pixel.ACTIVE);
    }

    public static Pixel of(char code) {
        return Arrays.stream(values())
                     .filter(pixel -> pixel.code == code)
                     .findAny()
                     .orElseThrow(() -> new IllegalArgumentException("Pixel " + code + " is not recognised."));
    }
}