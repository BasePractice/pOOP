package ru.mifi.practice.vol8.streaming;

import lombok.SneakyThrows;
import org.bson.BsonSerializationException;
import org.bson.io.BsonInput;
import org.bson.io.BsonInputMark;
import org.bson.types.ObjectId;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;

public final class Bson {
    public static final class BsonInputStream implements BsonInput {
        private static final String[] ONE_BYTE_ASCII_STRINGS = new String[Byte.MAX_VALUE + 1];

        static {
            for (int b = 0; b < ONE_BYTE_ASCII_STRINGS.length; b++) {
                ONE_BYTE_ASCII_STRINGS[b] = String.valueOf((char) b);
            }
        }

        private final PosBufferedInputStream stream;

        public BsonInputStream(InputStream stream) {
            this.stream = new PosBufferedInputStream(stream);
        }

        @Override
        public int getPosition() {
            return stream.getPosition();
        }

        @SneakyThrows
        @Override
        public byte readByte() {
            ensureAvailable(1);
            return (byte) stream.read();
        }

        @SneakyThrows
        @Override
        public void readBytes(byte[] bytes) {
            ensureAvailable(bytes.length);
            stream.read(bytes);
        }

        @SneakyThrows
        @Override
        public void readBytes(byte[] bytes, int offset, int length) {
            ensureAvailable(length);
            stream.read(bytes, 0, length);
        }

        @Override
        public long readInt64() {
            ensureAvailable(8);
            byte[] bytes = new byte[8];
            readBytes(bytes);
            return Utils.readInt64(bytes, 0);
        }

        @Override
        public double readDouble() {
            ensureAvailable(8);
            byte[] bytes = new byte[8];
            readBytes(bytes);
            return Utils.readInt64(bytes, 0);
        }

        @Override
        public int readInt32() {
            ensureAvailable(4);
            byte[] bytes = new byte[4];
            readBytes(bytes);
            return Utils.readInt32(bytes, 0);
        }

        @Override
        public String readString() {
            int size = readInt32();
            if (size <= 0) {
                throw new BsonSerializationException(format("While decoding a BSON string found a size that is not a positive number: %d",
                    size));
            }
            ensureAvailable(size);
            return readString(size);
        }

        private String readString(final int size) {
            if (size == 2) {
                byte asciiByte = readByte();               // if only one byte in the string, it must be ascii.
                byte nullByte = readByte();                // read null terminator
                if (nullByte != 0) {
                    throw new BsonSerializationException("Found a BSON string that is not null-terminated");
                }
                if (asciiByte < 0) {
                    return StandardCharsets.UTF_8.newDecoder().replacement();
                }
                return ONE_BYTE_ASCII_STRINGS[asciiByte];  // this will throw if asciiByte is negative
            } else {
                byte[] bytes = new byte[size - 1];
                readBytes(bytes);
                byte nullByte = readByte();
                if (nullByte != 0) {
                    throw new BsonSerializationException("Found a BSON string that is not null-terminated");
                }
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }

        @Override
        public ObjectId readObjectId() {
            byte[] bytes = new byte[12];
            readBytes(bytes);
            return new ObjectId(bytes);
        }

        @SneakyThrows
        @Override
        public String readCString() {
            int mark = getPosition();
            stream.mark(1024);
            skipCString();
            int size = getPosition();
            stream.reset();
            return readString(size - mark);
        }

        @Override
        public void skipCString() {
            boolean checkNext = true;
            while (checkNext) {
                if (!hasRemaining()) {
                    throw new BsonSerializationException("Found a BSON string that is not null-terminated");
                }
                checkNext = readByte() != 0;
            }
        }

        @SneakyThrows
        @Override
        public void skip(int numBytes) {
            stream.skip(numBytes);
        }

        @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
        @Override
        public BsonInputMark getMark(int readLimit) {
            stream.mark(readLimit);
            return new BsonInputMark() {
                @SneakyThrows
                @Override
                public void reset() {
                    stream.reset();
                }
            };
        }

        @SneakyThrows
        @Override
        public boolean hasRemaining() {
            return stream.available() > 0;
        }

        @SneakyThrows
        @Override
        public void close() {
            stream.close();
        }

        @SneakyThrows
        private void ensureAvailable(final int bytesNeeded) {
            if (stream.available() < bytesNeeded) {
                throw new BsonSerializationException(format("While decoding a BSON document %d bytes were required, "
                    + "but only %d remain", bytesNeeded, stream.available()));
            }
        }
    }

    private static final class PosBufferedInputStream extends BufferedInputStream {

        public PosBufferedInputStream(InputStream in) {
            super(in);
        }

        public int getPosition() {
            return pos;
        }
    }
}
