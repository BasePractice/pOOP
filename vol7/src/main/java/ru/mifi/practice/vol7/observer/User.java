package ru.mifi.practice.vol7.observer;

import java.util.Observable;


@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
public final class User extends Observable {
    private final String name;
    private String password;

    public User(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
        setChanged();
        notifyObservers(password);
    }

    public static void main(String[] args) {
        User user = new User("user");
        user.addObserver(new PrintObserver());
        user.setPassword("password");

    }
}
