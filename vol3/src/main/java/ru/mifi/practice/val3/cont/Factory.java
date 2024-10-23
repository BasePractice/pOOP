package ru.mifi.practice.val3.cont;

@FunctionalInterface
public interface Factory<T> {
    T create();
}
