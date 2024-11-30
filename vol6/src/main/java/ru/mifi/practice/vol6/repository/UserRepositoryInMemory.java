package ru.mifi.practice.vol6.repository;

import com.google.common.reflect.TypeToken;
import ru.mifi.practice.vol6.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class UserRepositoryInMemory implements RepositoryMutant<User, String> {
    private static final Type LIST = new TypeToken<ArrayList<User>>() {
    }.getType();
    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> search(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Type listType() {
        return LIST;
    }

    @Override
    public void add(User user) {
        if (users.containsKey(user.username())) {
            throw new IllegalArgumentException("User already exists");
        }
        users.put(user.username(), user);
    }

    @Override
    public void delete(String key) {
        users.remove(key);
    }

    @Override
    public void addAll(List<User> items) {
        items.forEach(this::add);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
