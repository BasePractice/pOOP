package ru.mifi.practice.vol6.storege;

import ru.mifi.practice.vol6.repository.RepositoryMutant;
import ru.mifi.practice.vol6.repository.Repository;

public interface Storage {

    <T> void write(Repository<T, String> repository);

    <T> RepositoryMutant<T, String> read(RepositoryMutant<T, String> repository);

    static Storage file() {
        return new FileStorage();
    }
}
