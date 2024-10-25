package ru.mifi.practice.vol6.storege;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.mifi.practice.vol6.repository.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class FileStorage implements Storage {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path users = Path.of("users.json");

    @Override
    public <T> void write(Repository<T, String> repository) {
        try {
            Files.writeString(users, gson.toJson(repository.findAll()),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("PMD.EmptyCatchBlock")
    @Override
    public <T> Repository.Mutant<T, String> read(Repository.Mutant<T, String> repository) {
        try {
            List<T> list = gson.fromJson(Files.readString(users, StandardCharsets.UTF_8), repository.listType());
            repository.addAll(list);
        } catch (IOException e) {
            //Ignore
        }
        return repository;
    }
}
