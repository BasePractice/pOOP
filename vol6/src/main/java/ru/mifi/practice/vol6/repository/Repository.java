package ru.mifi.practice.vol6.repository;

import ru.mifi.practice.vol6.Storage;

import java.util.Optional;

public interface Repository<T, I> {
    Optional<T> search(I id);

    void store(Storage storage);
}
