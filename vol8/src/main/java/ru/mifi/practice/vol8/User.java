package ru.mifi.practice.vol8;

import lombok.RequiredArgsConstructor;
import org.bson.BsonBinaryWriter;
import org.bson.BsonDocument;
import org.bson.io.BasicOutputBuffer;

@RequiredArgsConstructor
public final class User {
    private final String name;
    private final String password;

    public static void main(String[] args) {
        BsonDocument bson = new BsonDocument();
        User user = new User("bob", "password");
        try (BsonBinaryWriter writer = new BsonBinaryWriter(new BasicOutputBuffer())) {
            writer.writeStartDocument();
            writer.writeString("name", user.name);
            writer.writeString("password", user.password);
            writer.writeEndDocument();
            writer.flush();
        }
        System.out.println(bson.toJson());
    }
}
