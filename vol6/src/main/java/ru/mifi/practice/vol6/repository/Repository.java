package ru.mifi.practice.vol6.repository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public interface Repository<T, I> {

    List<T> findAll();

    Optional<T> search(I id);

    Type listType();

    interface Mutant<T, I> extends Repository<T, I> {

        void add(T item);

        void delete(I key);

        void addAll(List<T> items);
    }
}
