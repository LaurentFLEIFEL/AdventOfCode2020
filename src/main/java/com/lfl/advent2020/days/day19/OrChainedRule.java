package com.lfl.advent2020.days.day19;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.IntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.Arrays;

@Slf4j
public class OrChainedRule implements Rule {

    private int name;
    private Rule rule1;
    private Rule rule2;
    private IntList indexes;

    public static OrChainedRule of(int name, IntList ruleNames1, IntList ruleNames2, IntObjectMap<Rule> rules) {
        OrChainedRule result = new OrChainedRule();
        result.name = name;
        result.rule1 = ChainedRule.of(-1, ruleNames1, rules);
        result.rule2 = ChainedRule.of(-2, ruleNames2, rules);
        return result;
    }

    public static OrChainedRule of(String line, IntObjectMap<Rule> rules) {
        String[] split = line.split(": ");
        int name = Integer.parseInt(split[0]);
        String[] split2 = split[1].split(" \\| ");
        IntList ruleNames1 = Arrays.stream(split2[0].split(" "))
                                   .mapToInt(Integer::parseInt)
                                   .collect(IntLists.mutable::empty,
                                            MutableIntCollection::add,
                                            MutableIntCollection::addAll);
        IntList ruleNames2 = Arrays.stream(split2[1].split(" "))
                                   .mapToInt(Integer::parseInt)
                                   .collect(IntLists.mutable::empty,
                                            MutableIntCollection::add,
                                            MutableIntCollection::addAll);
        return OrChainedRule.of(name, ruleNames1, ruleNames2, rules);
    }

    @Override
    public int name() {
        return name;
    }

    @Override
    public boolean validate(String message, IntList indexes) {
        MutableIntList newIndexes = IntLists.mutable.empty();
        boolean hasBeenValidated = false;

        boolean validate = rule1.validate(message, indexes);
        if (validate) {
            hasBeenValidated = true;
            newIndexes.addAll(rule1.newIndexes());
        }
        boolean validate2 = rule2.validate(message, indexes);
        if (validate2) {
            hasBeenValidated = true;
            newIndexes.addAll(rule2.newIndexes());
        }

        this.indexes = newIndexes;

        return hasBeenValidated;
    }

    @Override
    public IntList newIndexes() {
        return indexes;
    }

    @Override
    public String toString() {
        return name
               + ": " + rule1.toString()
               + " | " + rule2.toString();
    }
}
