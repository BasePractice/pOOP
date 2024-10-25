package ru.mifi.practice.vol6.security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public interface Security {

    static Hash create() {
        return (password) -> Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    @FunctionalInterface
    interface Hash {
        String hash(String password);
    }
}
