package ru.mifi.practice.vol6.security;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public interface Security {

    static Hash createHash() {
        return HashType.SHA256;
    }

    enum HashType implements Hash {
        SHA256(Hashing.sha256());
        private final HashFunction hashFunction;

        HashType(HashFunction hashFunction) {
            this.hashFunction = hashFunction;
        }

        public String hash(String password) {
            return hashFunction.hashString(password, StandardCharsets.UTF_8).toString();
        }
    }

    @FunctionalInterface
    interface Hash {
        String hash(String password);
    }
}
