package ru.mifi.practice.vol6;

public interface Security {
    interface Hash {
        String hash(String password);
    }
}
