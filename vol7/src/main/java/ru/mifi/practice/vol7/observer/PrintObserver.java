package ru.mifi.practice.vol7.observer;

import java.util.Observable;
import java.util.Observer;

public final class PrintObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.printf("%s(%s)%n", o, arg);
    }
}
