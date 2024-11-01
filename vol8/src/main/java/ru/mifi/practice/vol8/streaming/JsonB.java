package ru.mifi.practice.vol8.streaming;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings({"PMD.CloseResource", "PMD.EmptyCatchBlock"})
public abstract class JsonB {
    /**
     * Генерация в потоке JSON с результирующим объемом около 200 мегабайт с последующим размещением в таблице с
     * полем типа JSONB которое индексировано для поиска. Занимаемое время ~40 секунд
     * <p>
     * streaming - 200 Mb, время вставки в finish - 37707 ms
     * <p>
     * CREATE TABLE IF NOT EXISTS finished (
     *   id      SERIAL CONSTRAINT finished_pk PRIMARY KEY,
     *   content jsonb NOT NULL
     * );
     * <p>
     * CREATE INDEX IF NOT EXISTS finished_content_index ON finished USING GIN (content);
     * <p>
     * <p>
     * CREATE TABLE streaming(
     *   id      SERIAL CONSTRAINT streaming_pk PRIMARY KEY,
     *   content TEXT NOT NULL
     * );
     */
    private static final int LARGE = 8000000;

    public static void main(String[] args) throws IOException {
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis);
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(pos);
        new Thread(() -> {
            long millis = System.currentTimeMillis();
            try {
                long streamKey = -1;
                try (var c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/bson", "postgres", "postgres")) {
                    c.setAutoCommit(false);
                    try (var stmt = c.prepareStatement("INSERT INTO streaming(content) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setCharacterStream(1, new InputStreamReader(pis));
                        stmt.executeUpdate();
                        try (var rs = stmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                streamKey = rs.getLong(1);
                            }
                        }
                    }
                    if (streamKey < 0) {
                        throw new SQLException("Streaming failed");
                    }
                    try (var stmt = c.prepareStatement("INSERT INTO finished(content) SELECT content::jsonb FROM streaming WHERE id = ?")) {
                        stmt.setLong(1, streamKey);
                        stmt.executeUpdate();
                    }
                    try (var stmt = c.prepareStatement("DELETE FROM streaming WHERE id = ?")) {
                        stmt.setLong(1, streamKey);
                        stmt.executeUpdate();
                    }
                    c.commit();
                    c.setAutoCommit(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    pis.close();
                } catch (IOException e) {
                    ex.printStackTrace();
                }
            } finally {
                System.out.printf("Time: %d ms%n", System.currentTimeMillis() - millis);
            }
        }).start();
        generator.writeStartArray();
        for (int i = 0; i < LARGE; i++) {
            generator.writeStartObject();
            generator.writeNumberField("id", i);
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.close();
        pos.close();
    }
}
