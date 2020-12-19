package com.lfl.advent2020.days.day19;

import org.eclipse.collections.api.list.primitive.IntList;

public interface Rule {
    int name();

    boolean validate(String message, IntList indexes);

    IntList newIndexes();
}
