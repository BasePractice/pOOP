package ru.mifi.practice.vol6.menu;

import ru.mifi.practice.vol6.security.Authentication;
import ru.mifi.practice.vol6.transport.Input;
import ru.mifi.practice.vol6.transport.Output;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class Context implements Output, Input {
    private final Output output;
    private final Input input;
    private final AtomicReference<Authentication.Session> session;
    private final Runnable onExit;

    public Context(Output output, Input input, Runnable onExit) {
        this.output = output;
        this.input = input;
        this.session = new AtomicReference<>();
        this.onExit = onExit;
    }

    public Context(Runnable onExit) {
        this(Output.DEFAULT, Input.DEFAULT, onExit);
    }


    @Override
    public void close() throws Exception {
        input.close();
    }

    @Override
    public String inputString() {
        return input.inputString();
    }

    @Override
    public Optional<Number> inputNumber() {
        return input.inputNumber();
    }

    @Override
    public void print(String format, Object... args) {
        output.print(format, args);
    }

    public void print() {
        String authenticated = "не авторизован";
        Authentication.Session session = this.session.get();
        if (session != null) {
            authenticated = session.user().username();
        }
        output.println("Auth: %s", authenticated);
    }

    @Override
    public void error(String format, Object... args) {
        output.error(format, args);
    }

    public String select(String text) {
        output.print(text);
        output.print("> ");
        return input.inputString();
    }

    public void putSession(Authentication.Session session) {
        this.session.compareAndExchange(null, session);
    }

    public void clearSession() {
        session.setRelease(null);
    }

    public Optional<Authentication.Session> authorized() {
        return Optional.ofNullable(session.get());
    }

    public void exit() {
        if (onExit != null) {
            onExit.run();
        }
    }
}
