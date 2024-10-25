package ru.mifi.practice.vol6.storege;

import ru.mifi.practice.vol6.repository.Repository;

public interface Storage {

    <T> void write(Repository<T, String> repository);

    <T> Repository.Mutant<T, String> read(Repository.Mutant<T, String> repository);

    static Storage file() {
        return new FileStorage();
    }
}
