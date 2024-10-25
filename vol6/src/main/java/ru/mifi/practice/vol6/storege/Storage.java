package ru.mifi.practice.vol6.storege;

import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;

public interface Storage {

    void write(Repository.Mutant<User, String> repository);

    Repository.Mutant<User, String> read(Repository.Mutant<User, String> repository);

    final class Empty implements Storage {

        @Override
        public void write(Repository.Mutant<User, String> repository) {
            System.err.println("запись для users не реализована");
        }

        @Override
        public Repository.Mutant<User, String> read(Repository.Mutant<User, String> repository) {
            System.err.println("загрузка для users не реализована");
            return repository;
        }
    }
}
