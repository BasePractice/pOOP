package ru.mifi.practice.val3.cont;

import java.util.Optional;

public interface Http {
    <T> Optional<T> get(String url, Class<T> clazz);
}
