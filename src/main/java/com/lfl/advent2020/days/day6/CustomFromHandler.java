package com.lfl.advent2020.days.day6;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.springframework.stereotype.Service;

import java.util.BitSet;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Service
public class CustomFromHandler implements LinesConsumer {

    @Getter
    private int sum;

    @Override
    public void consume(List<String> lines) {
        List<BitSet> groups = parseForms(lines);

        sum = groups.stream()
                    .mapToInt(BitSet::cardinality)
                    .sum();

        log.info("Sum = {}", sum);
    }

    private List<BitSet> parseForms(List<String> lines) {
        List<BitSet> groups = Lists.mutable.empty();
        List<BitSet> group = Lists.mutable.empty();
        for (String line : lines) {
            if (line.isEmpty()) {
                groups.add(aggregateForms(group));
                group = Lists.mutable.empty();
                continue;
            }

            group.add(buildBitForm(line));
        }
        groups.add(aggregateForms(group));
        return groups;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private BitSet aggregateForms(List<BitSet> group) {
        if (isEmpty(group)) {
            return new BitSet();
        }
        return group.stream()
                    .reduce((bitSet1, bitSet2) -> {
                        bitSet1.and(bitSet2);//.or for part 1
                        return bitSet1;
                    })
                    .get();
    }

    private BitSet buildBitForm(String line) {
        BitSet form = new BitSet();
        line.chars()
            .map(c -> c - 'a')//can work without it but the indexes are clearer (O -> a, 1 -> b, ..., 25 -> z)
            .forEach(form::set);
        return form;
    }
}
