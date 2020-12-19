package com.lfl.advent2020.days.day19;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FinalRule implements Rule {

    private static final Pattern finalRulePattern = Pattern.compile("(?<name>\\d+): \"(?<c>.)\"");

    private int name;
    private char c;
    private IntList indexes;

    public static FinalRule of(int name, char c) {
        FinalRule result = new FinalRule();
        result.name = name;
        result.c = c;
        return result;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static FinalRule of(String line) {
        Matcher matcher = finalRulePattern.matcher(line);
        matcher.find();
        int name = Integer.parseInt(matcher.group("name"));
        char c = matcher.group("c").charAt(0);
        return FinalRule.of(name, c);
    }

    @Override
    public int name() {
        return name;
    }

    @Override
    public boolean validate(String message, IntList indexes) {
        IntList newIndexes = indexes.primitiveStream()
                                    .filter(index -> index < message.length())
                                    .filter(index -> message.charAt(index) == c)
                                    .map(index -> index + 1)
                                    .collect(IntLists.mutable::empty,
                                             MutableIntCollection::add,
                                             MutableIntCollection::addAll);

        this.indexes = newIndexes;
        return !newIndexes.isEmpty();
    }

    @Override
    public IntList newIndexes() {
        return indexes;
    }

    @Override
    public String toString() {
        return name + ": \"" + c + "\"";
    }
}
