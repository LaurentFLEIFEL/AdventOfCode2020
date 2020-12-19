package com.lfl.advent2020.days.day19;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.IntObjectMap;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class OrChainedRule implements Rule {

    private int name;
    private MutableList<Rule> rulesToApply;
    private IntList indexes;

    public static OrChainedRule of(int name, List<IntList> ruleNames, IntObjectMap<Rule> rules) {
        OrChainedRule result = new OrChainedRule();
        result.name = name;
        result.rulesToApply = IntStream.range(0, ruleNames.size())
                                       .mapToObj(index -> ChainedRule.of(-index - 1, ruleNames.get(index), rules))
                                       .collect(Collectors2.toList());
        return result;
    }

    public static OrChainedRule of(String line, IntObjectMap<Rule> rules) {
        String[] split = line.split(": ");
        int name = Integer.parseInt(split[0]);
        String[] split2 = split[1].split(" \\| ");

        List<IntList> ruleNames = Arrays.stream(split2)
                                        .map(s -> Arrays.stream(s.split(" "))
                                                        .mapToInt(Integer::parseInt)
                                                        .collect(IntLists.mutable::empty,
                                                                 MutableIntCollection::add,
                                                                 MutableIntCollection::addAll))
                                        .collect(Collectors2.toList());

        return OrChainedRule.of(name, ruleNames, rules);
    }

    @Override
    public int name() {
        return name;
    }

    @Override
    public boolean validate(String message, IntList indexes) {
        MutableIntList newIndexes = IntLists.mutable.empty();
        boolean hasBeenValidated = false;

        for (Rule rule : rulesToApply) {
            boolean validate = rule.validate(message, indexes);
            if (validate) {
                hasBeenValidated = true;
                newIndexes.addAll(rule.newIndexes());
            }
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
               + ": " + rulesToApply.makeString(" | ");
    }
}
