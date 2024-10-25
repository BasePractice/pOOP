package ru.mifi.practice.vol6;

import ru.mifi.practice.vol6.model.User;
import ru.mifi.practice.vol6.repository.Repository;
import ru.mifi.practice.vol6.repository.UserRepositoryInMemory;
import ru.mifi.practice.vol6.storege.FileStorage;
import ru.mifi.practice.vol6.storege.Storage;

import java.util.Optional;

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

        root.addSub("Аутентификация", context -> {
            String login = context.select("Введите логин");
            String password = context.select("Введите пароль");
            Optional<Authentication.Context> sc = authentication.authenticate(login, password);
            sc.ifPresent(context::putContext);
            if (sc.isEmpty()) {
                context.clearContext();
                context.errorln("Не удалось авторизоваться под пользователем '%s'", login);
            }
        });
        root.addSub("Регистрация", context -> {
            String login = context.select("Введите логин");
            Optional<User> search = repository.search(login);
            if (search.isEmpty()) {
                String password = context.select("Введите пароль");
                String confirm = context.select("Подтвердите пароль");
                if (password.equals(confirm)) {
                    context.clearContext();
                    password = authentication.hash(password);
                    User user = new User(login, password);
                    repository.add(user);
                    context.putContext(Authentication.Context.of(user));
                } else {
                    context.errorln("Пароль не совпадает");
                }
            } else {
                context.errorln("Пользователь с именем '%s' уже существует", login);
            }
        });
        return () -> storage.write(repository);
    }
}
