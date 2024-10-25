package ru.mifi.practice.vol6;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface Authentication extends Security.Hash {

    Optional<Context> authenticate(String username, String password);

    interface Context {

        static Context of(User user) {
            return new SimpleContext(user);
        }

        User user();
    }

    record SimpleContext(User user) implements Context {
    }

    final class Default implements Authentication {
        private final HashFunction hf;
        private final Repository<User, String> repository;

        public Default(Repository<User, String> repository) {
            this.repository = repository;
            this.hf = Hashing.sha256();
        }

        @Override
        public Optional<Context> authenticate(String username, String password) {
            return repository.search(username)
                .filter(user -> user.equalsSecret(password, this))
                .map(Context::of);
        }

        @Override
        public String hash(String password) {
            return hf.hashString(password, StandardCharsets.UTF_8).toString();
        }
    }
}
