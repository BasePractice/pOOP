package ru.mifi.practice.vol6;

import ru.mifi.practice.vol6.repository.UserRepository;

import java.util.Optional;

public interface Authentication extends Security.Hash {

    Optional<Context> authenticate(String username, String password);

    interface Context {
        static Context of(UserRepository.User user) {
            return new SimpleContext(user);
        }

        UserRepository.User user();
    }

    record SimpleContext(UserRepository.User user) implements Context {
    }

    final class Default implements Authentication {
        private final UserRepository repository;

        public Default(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        public Optional<Context> authenticate(String username, String password) {
            return repository.search(username)
                .filter(user -> user.equalsSecret(password, this))
                .map(Context::of);
        }

        @Override
        public String hash(String password) {
            return password;
        }
    }
}
