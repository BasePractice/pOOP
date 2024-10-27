package ru.mifi.practice.vol7.singletone;

@SuppressWarnings("PMD.UseUtilityClass")
public final class Singletone3 {

    public static Impl getInstance() {
        return Signletone.INSTANCE;
    }

    private enum Signletone implements Impl {
        INSTANCE;
    }

    public interface Impl {

    }
}
