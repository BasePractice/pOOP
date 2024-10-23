package ru.mifi.practice.val3.cont;

public interface Deserializer {
    <T> T deserialize(String text, Class<T> clazz);
}
