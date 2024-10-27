package ru.mifi.practice.vol7.decorator;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Predicate;

public class Decorator<T> extends AbstractList<T> implements List<T> {
    private final List<T> decorated;
    private final Predicate<T> predicate;

    public Decorator(List<T> decorated, Predicate<T> predicate) {
        this.decorated = decorated;
        this.predicate = predicate;
    }

    @Override
    public T set(int index, T element) {
        if (predicate.test(element)) {
            return decorated.set(index, element);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        if (predicate.test(element)) {
            decorated.add(index, element);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        return decorated.get(index);
    }

    @Override
    public int size() {
        return decorated.size();
    }
}
