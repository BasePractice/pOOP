package ru.mifi.practice.vol7.builder;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
public class User2 {
    private final String name;
    private final String password;

    public static void main(String[] args) {
        User2 user = User2.builder()
            .name("Name1")
            .password("Password1")
            .build();
        user = user.toBuilder()
            .name("Name2")
            .build();
    }
}
