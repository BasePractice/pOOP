package ru.mifi.practice.vol6.security;

import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;

import java.util.Optional;

public interface Authentication {

    static Authentication create(Repository<User, String> repository, Security.Hash hash) {
        return new Default(repository, hash);
    }

    Optional<Session> authenticate(String username, String password);

    interface Session {

        static Session of(User user) {
            return new SimpleSession(user);
        }

        User user();
    }

    record SimpleSession(User user) implements Session {
    }

    final class Default implements Authentication {
        private final Security.Hash hash;
        private final Repository<User, String> repository;

        private Default(Repository<User, String> repository, Security.Hash hash) {
            this.repository = repository;
            this.hash = hash;
        }

        @Override
        public Optional<Session> authenticate(String username, String password) {
            return repository.search(username)
                .filter(user -> user.equalsSecret(password, hash))
                .map(Session::of);
        }
    }
}
