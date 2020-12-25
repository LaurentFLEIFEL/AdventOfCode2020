package com.lfl.advent2020.days.day25;

import org.junit.Test;

import java.math.BigInteger;

public class HotelCardHackerTest {

    @Test
    public void name() {
        //Given
        HotelCardHacker service = new HotelCardHacker();
        int cardPublicKey = 13316116;
        int doorPublicKey = 13651422;
        int loopSize = service.findLoopSize(cardPublicKey);

        //When
        System.out.println("loopSize = " + loopSize);

        BigInteger encryptionKey = service.encrypt(doorPublicKey, loopSize);

        //Then
        System.out.println("encryptionKey = " + encryptionKey);
    }
}