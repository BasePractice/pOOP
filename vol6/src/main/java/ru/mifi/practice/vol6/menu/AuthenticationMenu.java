package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.Authentication;

import java.util.Optional;

public final class AuthenticationMenu extends AbstractMenu {
    private final Authentication authentication;

    public AuthenticationMenu(Authentication authentication) {
        super("Аутентификация");
        this.authentication = authentication;
    }

    @Override
    public void accept(Context context) {
        String login = context.select("Введите логин");
        String password = context.select("Введите пароль");
        Optional<Authentication.Context> sc = authentication.authenticate(login, password);
        sc.ifPresent(context::putContext);
        if (sc.isEmpty()) {
            context.clearContext();
            context.errorln("Не удалось авторизоваться под пользователем '%s'", login);
        }
    }
}
