package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.security.Authentication;

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
        Optional<Authentication.Session> session = authentication.authenticate(login, password);
        session.ifPresent(context::putSession);
        if (session.isEmpty()) {
            context.clearSession();
            context.errorln("Не удалось авторизоваться под пользователем '%s'", login);
        }
    }
}
