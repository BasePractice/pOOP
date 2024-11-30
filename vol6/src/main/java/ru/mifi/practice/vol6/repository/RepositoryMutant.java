package ru.mifi.practice.vol6.repository;

import java.util.List;

public interface RepositoryMutant<T, I> extends Repository<T, I> {

    void add(T item);

    void delete(I key);

    void addAll(List<T> items);
}
