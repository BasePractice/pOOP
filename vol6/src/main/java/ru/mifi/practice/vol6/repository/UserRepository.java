package ru.mifi.practice.vol6.repository;


import ru.mifi.practice.vol6.Storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.mifi.practice.vol6.Security.Hash;

public interface UserRepository extends Repository<UserRepository.User, String> {

    void register(User user);

    record User(String username, String secret) implements Serializable {

        public boolean equalsSecret(String password, Hash hash) {
            return secret.equals(hash.hash(password));
        }
    }

    final class Memory implements UserRepository {
        private final Map<String, User> users = new HashMap<>();

        @Override
        public Optional<User> search(String id) {
            return Optional.ofNullable(users.get(id));
        }

        @Override
        public void store(Storage storage) {
            storage.write(this);
        }

        @Override
        public void register(User user) {
            if (users.containsKey(user.username())) {
                throw new IllegalArgumentException("User already exists");
            }
            users.put(user.username(), user);
        }
    }
}
