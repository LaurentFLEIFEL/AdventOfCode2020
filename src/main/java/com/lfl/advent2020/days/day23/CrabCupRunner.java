package com.lfl.advent2020.days.day23;

import com.lfl.advent2020.LinesConsumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Service
public class CrabCupRunner implements LinesConsumer {

    private MutableIntList cups = IntLists.mutable.empty();

    @Setter
    private int maxMoveNbr = 10_000_000;

    @Setter
    private boolean isPart2 = true;

    @Setter
    private int nbrMax = 1_000_000;

    @Override
    public void consume(List<String> lines) {
        String[] split = lines.get(0).split("");
        MutableIntObjectMap<Cup> cups = IntObjectMaps.mutable.empty();
        Cup current;
        Cup first = null;
        Cup previous = null;
        for (int index = 0; index < split.length; index++) {
            int value = Integer.parseInt(split[index]);
            current = Cup.of(value);
            cups.put(value, current);
            if (index == 0) {
                first = current;
                previous = first;
                continue;
            }

            previous.next = current;
            previous = current;
        }

        if (isPart2) {
            int nbr = 10;
            while (nbr <= nbrMax) {
                current = Cup.of(nbr);
                cups.put(nbr, current);
                previous.next = current;
                previous = current;
                nbr++;
            }
        }

        previous.next = first;

        MutableIntList cupList = IntLists.mutable.withInitialCapacity(cups.size());
        current = first;
        for (int moveNbr = 0; moveNbr < maxMoveNbr; moveNbr++) {
            //log.info("-- move {} --", moveNbr + 1);

//            Cup cup = current.next;
//            cupList.add(current.value);
//            while (cup != current) {
//            cupList.add(cup.value);
//                cup = cup.next;
//            }
//            log.info("cups: {}", cupList);
//            cupList.clear();
            Cup firstMoving = current.next;
            Cup lastMoving = current.next.next.next;
            int destinationValue = current.value - 1;
            if (destinationValue == 0) {
                destinationValue = nbrMax;
            }
            while (destinationValue == firstMoving.value
                   || destinationValue == firstMoving.next.value
                   || destinationValue == lastMoving.value) {
                destinationValue = destinationValue - 1;
                if (destinationValue == 0) {
                    destinationValue = nbrMax;
                }
            }
            Cup destination = cups.get(destinationValue);

            current.next = lastMoving.next;
            lastMoving.next = destination.next;
            destination.next = firstMoving;

            current = current.next;
        }

        Cup one = cups.get(1);
        if (isPart2) {
            int onePlusOneValue = one.next.value;
            int onePlusTwoValue = one.next.next.value;

            BigInteger result = BigInteger.valueOf(onePlusOneValue).multiply(BigInteger.valueOf(onePlusTwoValue));
            log.info("result = '{}'", result);
        } else {
            current = one.next;
            String result = "";
            while (current.value != 1) {
                result += current.value;
                current = current.next;
            }
            log.info("result = '{}'", result);
        }


        //naive(lines);

    }

    private void naive(List<String> lines) {
        cups = Arrays.stream(lines.get(0).split(""))
                     .mapToInt(Integer::parseInt)
                     .collect(IntLists.mutable::empty,
                              MutableIntCollection::add,
                              MutableIntCollection::addAll);
        MutableIntIntMap valueByIndex = IntIntMaps.mutable.empty();
        IntStream.range(0, cups.size())
                 .forEach(index -> valueByIndex.put(cups.get(index), index));

        if (isPart2) {
            int nbr = 10;
            while (nbr <= nbrMax) {
                cups.add(nbr);
                valueByIndex.put(cups.size() - 1, nbr);
                nbr++;
            }
        }


        int currentIndex = 0;
        MutableIntList movedCups = IntLists.mutable.empty();
        MutableIntSet movedCupsSet = IntSets.mutable.empty();
        int destinationIndex;
        int destinationValue;
        for (int moveNbr = 0; moveNbr < maxMoveNbr; moveNbr++) {
            if (moveNbr % 1_000 == 0) {
                log.info("-- move {} --", moveNbr + 1);
            }
            log.info("-- move {} --", moveNbr + 1);
            log.info("cups: {}", cups);
            movedCups.add(getProperValue(currentIndex + 1));
            movedCups.add(getProperValue(currentIndex + 2));
            movedCups.add(getProperValue(currentIndex + 3));
            movedCupsSet.addAll(movedCups);

            log.info("pick up: {}", movedCups);
            destinationValue = (getProperValue(currentIndex) - 1 + nbrMax + 1) % (nbrMax + 1);
            if (destinationValue == 0) {
                destinationValue = nbrMax;
            }
            while (movedCupsSet.contains(destinationValue)) {
                destinationValue = (destinationValue - 1 + nbrMax + 1) % (nbrMax + 1);
                if (destinationValue == 0) {
                    destinationValue = nbrMax;
                }
            }

            log.info("destination: {}", destinationValue);
            destinationIndex = valueByIndex.get(destinationValue);

            int index = currentIndex + 4;
            while (getProperIndex(index) != destinationIndex) {
                cups.set(getProperIndex(index - 3), getProperValue(index));
                valueByIndex.put(getProperValue(index), getProperIndex(index - 3));
                index++;
            }
            cups.set(getProperIndex(destinationIndex - 3), destinationValue);
            valueByIndex.put(destinationValue, getProperIndex(destinationIndex - 3));
            cups.set(getProperIndex(destinationIndex - 2), movedCups.get(0));
            valueByIndex.put(movedCups.get(0), getProperIndex(destinationIndex - 2));
            cups.set(getProperIndex(destinationIndex - 1), movedCups.get(1));
            valueByIndex.put(movedCups.get(1), getProperIndex(destinationIndex - 1));
            cups.set(getProperIndex(destinationIndex), movedCups.get(2));
            valueByIndex.put(movedCups.get(2), getProperIndex(destinationIndex));

            currentIndex = getProperIndex(currentIndex + 1);
            movedCups.clear();
            movedCupsSet.clear();
            log.info("");
        }

        int one = valueByIndex.get(1);
        if (isPart2) {
            int onePlusOneValue = getProperValue(one + 1);
            int onePlusTwoValue = getProperValue(one + 2);

            BigInteger result = BigInteger.valueOf(onePlusOneValue).multiply(BigInteger.valueOf(onePlusTwoValue));
            log.info("result = '{}'", result);
        } else {
            int index = one + 1;
            String result = "";
            while (getProperIndex(index) != one) {
                result += getProperValue(index);
                index++;
            }
            log.info("result = '{}'", result);
        }
    }

    private int getProperValue(int index) {
        return cups.get(getProperIndex(index));
    }

    private int getProperIndex(int index) {
        return (index + cups.size()) % cups.size();
    }



    private static class Cup {
       public int value;
       public Cup next;

       public static Cup of(int value) {
           Cup cup = new Cup();
           cup.value = value;
           return cup;
       }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Cup cup = (Cup) o;
            return value == cup.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
}
