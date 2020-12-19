package com.lfl.advent2020.days.day19;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.IntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ChainedRule implements Rule {

    private int name;
    private IntList ruleNames;
    private IntObjectMap<Rule> rules;
    private IntList indexes;
    private MutableList<Rule> rulesToApply;

    public static ChainedRule of(int name, IntList ruleNames, IntObjectMap<Rule> rules) {
        ChainedRule result = new ChainedRule();
        result.name = name;
        result.ruleNames = ruleNames;
        result.rules = rules;
        return result;
    }

    public static ChainedRule of(String line, IntObjectMap<Rule> rules) {
        String[] split = line.split(": ");
        int name = Integer.parseInt(split[0]);
        MutableIntList ruleNames = Arrays.stream(split[1].split(" "))
                                         .mapToInt(Integer::parseInt)
                                         .collect(IntLists.mutable::empty,
                                                  MutableIntCollection::add,
                                                  MutableIntCollection::addAll);
        return ChainedRule.of(name, ruleNames, rules);
    }

    @Override
    public int name() {
        return name;
    }

    @Override
    public boolean validate(String message, IntList indexes) {
        initRules();
        for (Rule rule : rulesToApply) {
            boolean validate = rule.validate(message, indexes);
            indexes = rule.newIndexes();
            if (!validate) {
                return false;
            }
        }

        this.indexes = indexes;
        return true;
    }

    private void initRules() {
        if (Objects.nonNull(rulesToApply)) {
            return;
        }

        rulesToApply = ruleNames.collect(rules::get)
                                .toList();
    }

    @Override
    public IntList newIndexes() {
        return indexes;
    }

    @Override
    public String toString() {
        return name + ": " + ruleNames.makeString(" ");
    }
}
