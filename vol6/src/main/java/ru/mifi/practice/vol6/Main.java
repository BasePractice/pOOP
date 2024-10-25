package ru.mifi.practice.vol6;

import ru.mifi.practice.vol6.menu.AuthenticationMenu;
import ru.mifi.practice.vol6.menu.DeleteRegistrationMenu;
import ru.mifi.practice.vol6.menu.RegistrationMenu;
import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;
import ru.mifi.practice.vol6.repository.UserRepositoryInMemory;
import ru.mifi.practice.vol6.storege.FileStorage;
import ru.mifi.practice.vol6.storege.Storage;

public abstract class Main {
    public static void main(String[] args) throws Exception {
        Menu root = Menu.root();
        Storage storage = new FileStorage();
        Runnable onExit = prepare(root, storage);
        try (Menu.Context context = Menu.defaultContext(onExit)) {
            root.select(context);
        }
    }

    private static Runnable prepare(Menu root, Storage storage) {
        Repository.Mutant<User, String> repository = storage.read(new UserRepositoryInMemory());
        Authentication authentication = new Authentication.Default(repository);
        new AuthenticationMenu(authentication).register(root);
        new RegistrationMenu(repository, authentication).register(root);
        new DeleteRegistrationMenu(repository).register(root);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            storage.write(repository);
        }));
        return () -> storage.write(repository);
    }
}
