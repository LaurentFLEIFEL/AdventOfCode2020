package com.lfl.advent2020.days.day10;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.ImmutableIntIntMap;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
public class AdapterCharger implements LinesConsumer {
    private static final ImmutableIntIntMap arrangementValues = IntIntMaps.mutable.empty()
                                                                                  .withKeyValue(1, 1)
                                                                                  .withKeyValue(2, 2)
                                                                                  .withKeyValue(3, 4)
                                                                                  .withKeyValue(4, 7)
                                                                                  .toImmutable();
    @Getter
    private BigInteger arrangements;
    @Getter
    private int count1;
    @Getter
    private int count3;

    @Override
    public void consume(List<String> lines) {
        MutableIntList sortedAdapters = sortAdapters(lines);
        findPaths(sortedAdapters);
    }

    private void findPaths(MutableIntList sortedAdapters) {
        count1 = 0;
        count3 = 0;
        int count2 = 0;
        String adaptersDifferences = "";
        int count1Contiguous = 0;
        arrangements = BigInteger.ONE;
        for (int index = 1; index < sortedAdapters.size(); index++) {
            int previous = sortedAdapters.get(index - 1);
            int current = sortedAdapters.get(index);

            if (current - previous == 1) {
                adaptersDifferences += 1;
                count1Contiguous++;
                count1++;
            }

            if (current - previous == 2) {
                count2++;
            }

            if (current - previous == 3) {
                adaptersDifferences += 3;
                BigInteger localArrangements = BigInteger.valueOf(arrangementValues.getIfAbsent(count1Contiguous, 1));
                arrangements = arrangements.multiply(localArrangements);
                count1Contiguous = 0;
                count3++;
            }
        }

        log.info("adaptersDifferences = {}", adaptersDifferences);
        log.info("Count 1 = {}", count1);
        log.info("Count 2 = {}", count2);
        log.info("Count 3 = {}", count3);
        log.info("Product = {}", count1 * count3);
        log.info("arr = {}", arrangements);
    }

    private MutableIntList sortAdapters(List<String> lines) {
        lines.add("0");//charging outlet
        MutableIntList sortedAdapters = lines.stream()
                                             .mapToInt(Integer::parseInt)
                                             .sorted()
                                             .collect(() -> IntLists.mutable.withInitialCapacity(lines.size()),
                                                      MutableIntCollection::add,
                                                      MutableIntCollection::addAll);
        int max = sortedAdapters.max();
        sortedAdapters.add(max + 3);//built-in adapter
        return sortedAdapters;
    }
}
