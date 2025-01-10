package com.bulka.java.games.jmine.engine.utils;

public class MyMath {
    public static short getFirst12Bits(short value) {
        return (short) ((value >>> 4) & 0x0FFF);
    }

    public static byte getLast4Bits(short value) {
        return (byte) (value & 0x0F);
    }

    public static void splitBits(short value, short[] outputArray) {
        outputArray[0] = (short) ((value >>> 4) & 0x0FFF);
        outputArray[1] = (short) (value & 0x0F);
    }

    public static short combineBits(short shortValue, byte byteValue) {
        int first12Bits = shortValue & 0x0FFF;
        int last4Bits = byteValue & 0x0F;
        return (short) ((first12Bits << 4) | last4Bits);
    }
}
