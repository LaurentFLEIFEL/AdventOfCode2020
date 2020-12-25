package com.lfl.advent2020.days.day25;

import com.lfl.advent2020.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
public class HotelCardHacker implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {

    }

    public BigInteger encrypt(int subjectNumber, int loopSize) {
        BigInteger result = BigInteger.ONE;
        BigInteger sn = BigInteger.valueOf(subjectNumber);
        BigInteger m = BigInteger.valueOf(20201227);
        for (int i = 0; i < loopSize; i++) {
            result = result.multiply(sn);
            result = result.mod(m);
        }

        return result;
    }

    public int findLoopSize(int publicKey) {
        BigInteger result = BigInteger.ONE;
        BigInteger sn = BigInteger.valueOf(7);
        BigInteger m = BigInteger.valueOf(20201227);
        for (int i = 0; i < 20201227; i++) {
            result = result.multiply(sn);
            result = result.mod(m);
            if (result.intValue() == publicKey) {
                return i + 1;
            }
        }
        return -1;
    }
}
