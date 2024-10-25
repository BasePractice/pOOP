package ru.mifi.practice.vol6;

import ru.mifi.practice.vol6.repository.UserRepository;

public interface Storage {

    void write(UserRepository repository);

    void read(UserRepository repository);

    final class Empty implements Storage {

        @Override
        public void write(UserRepository repository) {
            System.err.println("запись для users не реализована");
        }

        @Override
        public void read(UserRepository repository) {
            System.err.println("загрузка для users не реализована");
        }
    }
}
