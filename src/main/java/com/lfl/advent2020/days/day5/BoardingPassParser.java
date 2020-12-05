package com.lfl.advent2020.days.day5;

import com.lfl.advent2020.LinesConsumer;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

@Slf4j
@Service
public class BoardingPassParser implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableIntSet allTakenSeatId = lines.stream()
                                            .map(BoardingPass::of)
                                            .mapToInt(BoardingPass::getSeatId)
                                            .collect(() -> IntSets.mutable.withInitialCapacity(lines.size()),
                                                     MutableIntCollection::add,
                                                     MutableIntCollection::addAll);

        int maxSeatID = allTakenSeatId.max();
        log.info("Max seat ID = {}", maxSeatID);

        MutableIntSet allValidSeatId = IntInterval.from(allTakenSeatId.min())
                                                  .to(maxSeatID)
                                                  .toSet();

        allValidSeatId.withoutAll(allTakenSeatId);
        if (allValidSeatId.size() != 1) {
            throw new IllegalArgumentException("Only one seat should be left empty. Here : " + allValidSeatId.toString());
        }
        log.info("Remaining seat ID = {}", allValidSeatId.min());
    }

    @Data
    public static class BoardingPass {
        int row;
        int column;

        public static BoardingPass of(int row, int column) {
            BoardingPass boardingPass = new BoardingPass();
            boardingPass.setRow(row);
            boardingPass.setColumn(column);
            return boardingPass;
        }

        public static BoardingPass of(String description) {
            if (description.length() != 10) {
                throw new IllegalArgumentException("BoardingPass description " + description + " is not recognised.");
            }

            String row = description.substring(0, 7);
            String column = description.substring(7, 10);

            return BoardingPass.of(retrieveValue(row), retrieveValue(column));
        }

        private static int retrieveValue(String description) {
            String binaryValue = description.chars()
                                            .mapToObj(c -> Partition.of((char) c))
                                            .map(Partition::getBinaryValue)
                                            .collect(Collector.of(
                                                    () -> new StringBuilder(description.length()),
                                                    StringBuilder::append,
                                                    StringBuilder::append,
                                                    StringBuilder::toString));

            return Integer.parseInt(binaryValue, 2);
        }

        public int getSeatId() {
            return row * 8 + column;
        }
    }

    public enum Partition {
        F('F', '0'),
        B('B', '1'),
        L('L', '0'),
        R('R', '1');

        private final char code;
        @Getter
        private final char binaryValue;

        Partition(char code, char binaryValue) {
            this.code = code;
            this.binaryValue = binaryValue;
        }

        public static Partition of(char code) {
            return Arrays.stream(values())
                         .filter(partition -> partition.code == code)
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException("Partition " + code + " is not recognised."));
        }
    }
}
