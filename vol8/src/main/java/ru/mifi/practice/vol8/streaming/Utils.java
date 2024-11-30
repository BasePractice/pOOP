package ru.mifi.practice.vol8.streaming;

import lombok.experimental.UtilityClass;

@UtilityClass
final class Utils {
    static int readInt16(byte[] buffer, int offset) {
        if (buffer.length < 2 + offset) {
            return -1;
        }
        return (buffer[1 + offset] << 8) + (buffer[0 + offset] << 0);
    }

    static int readInt32(byte[] buffer, int offset) {
        if (buffer.length < 4 + offset) {
            return -1;
        }
        int ch0 = buffer[3 + offset];
        int ch1 = buffer[2 + offset];
        int ch2 = buffer[1 + offset];
        int ch3 = buffer[0 + offset];
        return (ch0 << 24) + (ch1 << 16) + (ch2 << 8) + ch3;
    }

    @SuppressWarnings("PMD.UselessParentheses")
    static long readInt64(byte[] buffer, int offset) {
        if (buffer.length < 8 + offset) {
            return -1;
        }
        return (((long) buffer[7 + offset] << 56)
            + ((long) (buffer[6 + offset] & 255) << 48)
            + ((long) (buffer[5 + offset] & 255) << 40)
            + ((long) (buffer[4 + offset] & 255) << 32)
            + ((long) (buffer[3 + offset] & 255) << 24)
            + ((buffer[2 + offset] & 255) << 16)
            + ((buffer[1 + offset] & 255) << 8)
            + ((buffer[0 + offset] & 255) << 0));
    }

    void writeInt64(byte[] buffer, int offset, long v) {
        buffer[7 + offset] = (byte) (v >>> 56);
        buffer[6 + offset] = (byte) (v >>> 48);
        buffer[5 + offset] = (byte) (v >>> 40);
        buffer[4 + offset] = (byte) (v >>> 32);
        buffer[3 + offset] = (byte) (v >>> 24);
        buffer[2 + offset] = (byte) (v >>> 16);
        buffer[1 + offset] = (byte) (v >>> 8);
        buffer[0 + offset] = (byte) (v >>> 0);
    }

    static void writeInt32(byte[] buffer, int offset, int v) {
        buffer[3 + offset] = (byte) (v >>> 24);
        buffer[2 + offset] = (byte) (v >>> 16);
        buffer[1 + offset] = (byte) (v >>> 8);
        buffer[0 + offset] = (byte) (v >>> 0);
    }

    static void writeInt16(byte[] buffer, int offset, int v) {
        buffer[1 + offset] = (byte) (v >>> 8);
        buffer[0 + offset] = (byte) (v >>> 0);
    }
}
