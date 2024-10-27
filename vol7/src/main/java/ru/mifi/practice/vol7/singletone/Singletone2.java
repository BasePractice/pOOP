package ru.mifi.practice.vol7.singletone;

public final class Singletone2 {
    private Singletone2() {

    }

    public static Singletone2 getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final Singletone2 INSTANCE = new Singletone2();
    }
}
