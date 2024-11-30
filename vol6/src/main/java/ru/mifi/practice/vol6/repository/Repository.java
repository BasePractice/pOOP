package ru.mifi.practice.vol6.repository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public interface Repository<T, I> {

    List<T> findAll();

    Optional<T> search(I id);

    Type listType();

}
