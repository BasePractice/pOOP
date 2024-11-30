package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.repository.RepositoryMutant;
import ru.mifi.practice.vol6.security.Authentication;
import ru.mifi.practice.vol6.security.Security;
import ru.mifi.practice.vol6.model.User;

import java.util.Optional;

public final class RegistrationMenu extends AbstractMenu {
    private final RepositoryMutant<User, String> repository;
    private final Security.Hash hash;

    public RegistrationMenu(RepositoryMutant<User, String> repository, Security.Hash hash) {
        super("Регистрация");
        this.repository = repository;
        this.hash = hash;
    }

    @Override
    public void accept(Context context) {
        String login = context.select("Введите логин");
        Optional<User> search = repository.search(login);
        if (search.isEmpty()) {
            String password = context.select("Введите пароль");
            String confirm = context.select("Подтвердите пароль");
            if (password.equals(confirm)) {
                context.clearSession();
                password = hash.hash(password);
                User user = new User(login, password);
                repository.add(user);
                context.putSession(Authentication.Session.of(user));
            } else {
                context.errorln("Пароль не совпадает");
            }
        } else {
            context.errorln("Пользователь с именем '%s' уже существует", login);
        }
    }
}
