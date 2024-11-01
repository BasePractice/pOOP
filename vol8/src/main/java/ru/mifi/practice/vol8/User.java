package ru.mifi.practice.vol8;

import lombok.RequiredArgsConstructor;
import org.bson.AbstractBsonWriter;
import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.io.BasicOutputBuffer;
import ru.mifi.practice.vol8.streaming.Bson;

import java.io.ByteArrayInputStream;

@RequiredArgsConstructor
public final class User {
    private final String name;
    private final String password;

    public static void main(String[] args) {
        User user = new User("bob", "password");
        BasicOutputBuffer output = new BasicOutputBuffer();
        try (AbstractBsonWriter writer = new BsonBinaryWriter(output)) {
            writer.writeStartDocument();
            writer.writeString("name", user.name);
            writer.writeString("password", user.password);
            writer.writeEndDocument();
            writer.flush();
        }
        byte[] bytes = output.toByteArray();
        try (BsonReader reader = new BsonBinaryReader(new Bson.BsonInputStream(
            new ByteArrayInputStream(bytes)))) {
            boolean reading = true;
            while (reading) {
                BsonType type = reader.readBsonType();
                switch (type) {
                    case DOCUMENT: {
                        reader.readStartDocument();
                        System.out.println("Start document");
                        break;
                    }
                    case END_OF_DOCUMENT: {
                        System.out.println("End of document");
                        reading = false;
                        break;
                    }
                    case STRING: {
                        String readName = reader.readName();
                        if ("name".equals(readName)) {
                            System.out.println(reader.readString());
                        } else if ("password".equals(readName)) {
                            System.out.println(reader.readString());
                        }
                        break;
                    }
                    default: {
                        String name = reader.readName();
                        System.out.println(name);
                        break;
                    }
                }
            }
        }
    }
}
