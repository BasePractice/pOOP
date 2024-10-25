package ru.mifi.practice.vol6.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, I> {

    List<T> findAll();

    Optional<T> search(I id);

    interface Mutant<T, I> extends Repository<T, I> {

        void add(T item);

        void delete(I key);

        void addAll(List<T> items);
    }
}
