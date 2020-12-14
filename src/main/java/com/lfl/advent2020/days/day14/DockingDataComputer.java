package com.lfl.advent2020.days.day14;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.springframework.stereotype.Service;

import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class DockingDataComputer implements LinesConsumer {

    private static final Pattern memoryPattern = Pattern.compile("mem\\[(?<address>\\d+)] = (?<value>\\d+)");
    private MutableIntIntMap mask = IntIntMaps.mutable.empty();
    private MutableIntList maskX = IntLists.mutable.empty();
    private final MutableMap<BitSet, BitSet> memory1 = Maps.mutable.empty();
    private final MutableMap<BitSet, BitSet> memory2 = Maps.mutable.empty();
    @Getter
    private long sum1;

    @Getter
    private long sum2;

    @Setter
    private boolean doPart1 = true;
    @Setter
    private boolean doPart2 = true;

    @Override
    public void consume(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("mask")) {
                mask = IntIntMaps.mutable.empty();
                maskX = IntLists.mutable.empty();
                extractMask(line);
                continue;
            }

            MemoryInstruction memoryInstruction = extractMemoryInstruction(line);
            if (doPart1) {
                applyInstruction1(memoryInstruction);
            }
            if (doPart2) {
                applyInstruction2(memoryInstruction);
            }
        }

        sum1 = memory1.values()
                      .stream()
                      .mapToLong(bitSet -> bitSet.toLongArray()[0])
                      .sum();

        sum2 = memory2.values()
                      .stream()
                      .mapToLong(bitSet -> bitSet.toLongArray()[0])
                      .sum();

        log.info("sum1 = {}", sum1);
        log.info("sum2 = {}", sum2);
    }

    private void applyInstruction2(MemoryInstruction memoryInstruction) {
        BitSet value = memoryInstruction.getValue();
        BitSet address = memoryInstruction.getAddress();
        applyMask2(address).forEach(address1 -> memory2.put(address1, value));
    }

    private List<BitSet> applyMask2(BitSet address) {
        mask.forEachKey(index -> address.set(index, mask.get(index) == 1 || address.get(index)));

        MutableList<BitSet> results = Lists.mutable.of(address);
        for (int index : maskX.toArray()) {
            MutableList<BitSet> temp = Lists.mutable.withInitialCapacity(2 * results.size());
            for (BitSet addressIn : results) {
                addressIn.set(index, true);
                BitSet clone = BitSet.valueOf(addressIn.toLongArray());
                clone.set(index, false);
                temp.add(addressIn);
                temp.add(clone);
            }
            results = temp;
        }
        return results;
    }

    private void applyInstruction1(MemoryInstruction memoryInstruction) {
        BitSet value = memoryInstruction.getValue();
        applyMask1(value);
        memory1.put(memoryInstruction.getAddress(), value);
    }

    private void applyMask1(BitSet value) {
        mask.forEachKey(index -> value.set(index, mask.get(index) == 1));
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private MemoryInstruction extractMemoryInstruction(String line) {
        Matcher matcher = memoryPattern.matcher(line);
        matcher.find();
        int address = Integer.parseInt(matcher.group("address"));
        int value = Integer.parseInt(matcher.group("value"));
        return MemoryInstruction.of(address, value);
    }

    private void extractMask(String line) {
        String sMask = line.split(" = ")[1];
        for (int index = 0; index < sMask.length(); index++) {
            int newIndex = sMask.length() - index - 1;
            char c = sMask.charAt(newIndex);
            if (c == 'X') {
                maskX.add(index);
                continue;
            }

            mask.put(index, c - '0');
        }
    }

    @Getter
    @Setter
    private static class MemoryInstruction {
        private int address;
        private int value;

        public static MemoryInstruction of(int address, int value) {
            MemoryInstruction result = new MemoryInstruction();
            result.setAddress(address);
            result.setValue(value);
            return result;
        }

        public BitSet getValue() {
            BitSet bitSet = new BitSet(36);
            bitSet.or(BitSet.valueOf(new long[]{value}));
            return bitSet;
        }

        public BitSet getAddress() {
            BitSet bitSet = new BitSet(36);
            bitSet.or(BitSet.valueOf(new long[]{address}));
            return bitSet;
        }
    }
}
