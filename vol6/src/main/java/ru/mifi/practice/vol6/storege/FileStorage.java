package ru.mifi.practice.vol6.storege;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public final class FileStorage implements Storage {
    private static final Type LIST_OF_USERS = new TypeToken<ArrayList<User>>() {
    }.getType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path users = Path.of("users.json");

    @Override
    public void write(Repository.Mutant<User, String> repository) {
        try {
            Files.writeString(users, gson.toJson(repository.findAll()),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Repository.Mutant<User, String> read(Repository.Mutant<User, String> repository) {
        try {
            List<User> list = gson.fromJson(Files.readString(users, StandardCharsets.UTF_8), LIST_OF_USERS);
            repository.addAll(list);
        } catch (IOException e) {
            //Ignore
        }
        return repository;
    }
}
