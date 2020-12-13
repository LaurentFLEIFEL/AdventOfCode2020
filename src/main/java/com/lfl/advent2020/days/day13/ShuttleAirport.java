package com.lfl.advent2020.days.day13;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.collection.primitive.MutableIntCollection;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.impl.factory.primitive.IntIntMaps;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

@Slf4j
@Service
public class ShuttleAirport implements LinesConsumer {

    @Getter
    private int result1;
    @Getter
    private BigInteger result2;

    @Override
    public void consume(List<String> lines) {
        //part1(lines);
        part2(lines);

    }

    private void part2(List<String> lines) {
        String[] input = lines.get(1).split(",");
        MutableIntList indexes = IntLists.mutable.empty();
        MutableList<BigInteger> numbers = Lists.mutable.empty();
        IntStream.range(0, input.length)
                 .filter(index -> !"x".equals(input[index]))
                 .forEach(index -> {
                     indexes.add(index);
                     numbers.add(BigInteger.valueOf(Integer.parseInt(input[index])));
                 });

        BigInteger product = numbers.reduce(BigInteger::multiply).get();
        MutableList<BigInteger> partialProducts = numbers.collect(product::divide);

        MutableList<BigInteger> inverse = IntStream.range(0, numbers.size())
                                                   .mapToObj(index -> computeInverse(partialProducts.get(index), numbers.get(index)))
                                                   .collect(toCollection(() -> Lists.mutable.withInitialCapacity(numbers.size())));

        BigInteger sum = BigInteger.ZERO;
        for (int index = 0; index < numbers.size(); index++) {
            sum = sum.add(partialProducts.get(index)
                                         .multiply(inverse.get(index))
                                         .multiply(BigInteger.valueOf(-indexes.get(index))));
        }

        result2 = sum.mod(product);

        log.info("Result2 = {}", result2);
    }

    public static BigInteger computeInverse(BigInteger a, BigInteger b) {
        BigInteger m = b;
        BigInteger t;
        BigInteger q;
        BigInteger x = BigInteger.ZERO;
        BigInteger y = BigInteger.ONE;
        if (b.equals(BigInteger.ONE))
            return BigInteger.ZERO;
        // Apply extended Euclid Algorithm
        while (a.compareTo(BigInteger.ONE) > 0) {
            // q is quotient
            q = a.divide(b);
            t = b;
            // now proceed same as Euclid's algorithm
            b = a.mod(b);
            a = t;
            t = x;
            x = y.subtract(q.multiply(x));
            y = t;
        }
        // Make positive
        if (y.compareTo(BigInteger.ZERO) < 0)
            y = y.add(m);
        return y;
    }

    private void part1(List<String> lines) {
        int earliestTimestamp = Integer.parseInt(lines.get(0));
        MutableIntList buses = Arrays.stream(lines.get(1).split(","))
                                     .filter(token -> !"x".equals(token))
                                     .mapToInt(Integer::parseInt)
                                     .sorted()
                                     .collect(IntLists.mutable::empty,
                                              MutableIntCollection::add,
                                              MutableIntCollection::addAll);
        log.info("Buses   = {}", buses);

        MutableIntList modulos = buses.primitiveStream()
                                      .map(id -> id - earliestTimestamp % id)
                                      .collect(IntLists.mutable::empty,
                                               MutableIntCollection::add,
                                               MutableIntCollection::addAll);

        log.info("Modulos = {}", modulos);
        int min = modulos.min();
        int indexOfMin = modulos.indexOf(min);
        int busId = buses.get(indexOfMin);

        log.info("Min = {}", min);
        log.info("BusId = {}", busId);

        result1 = min * busId;
        log.info("result1 = {}", result1);
    }
}
