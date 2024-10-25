package ru.mifi.practice.vol6;

import ru.mifi.practice.vol6.repository.UserRepository;

import java.util.Optional;

public abstract class Main {
    public static void main(String[] args) throws Exception {
        Menu root = Menu.root();
        Storage storage = new Storage.Empty();
        Runnable onExit = prepare(root, storage);
        try (Menu.Context context = Menu.defaultContext(onExit)) {
            root.select(context);
        }
    }

    private static Runnable prepare(Menu root, Storage storage) {
        UserRepository repository = new UserRepository.Memory();
        Authentication authentication = new Authentication.Default(repository);

        root.addSub("Аутентификация", context -> {
            String login = context.select("Введите логин");
            String password = context.select("Введите пароль");
            Optional<Authentication.Context> sc = authentication.authenticate(login, password);
            sc.ifPresent(context::putContext);
            if (sc.isEmpty()) {
                context.clearContext();
                context.error("Не удалось авторизоваться под пользователем '%s'", login);
            }
        });
        root.addSub("Регистрация", context -> {
            String login = context.select("Введите логин");
            Optional<UserRepository.User> search = repository.search(login);
            if (search.isEmpty()) {
                String password = context.select("Введите пароль");
                String confirm = context.select("Подтвердите пароль");
                if (password.equals(confirm)) {
                    context.clearContext();
                    password = authentication.hash(password);
                    UserRepository.User user = new UserRepository.User(login, password);
                    repository.register(user);
                    context.putContext(Authentication.Context.of(user));
                } else {
                    context.error("Пароль не совпадает");
                }
            } else {
                context.error("Пользователь с именем '%s' уже существует", login);
            }
        });
        storage.read(repository);
        return () -> repository.store(storage);
    }
}
