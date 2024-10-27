package ru.mifi.practice.vol7.singletone;

@SuppressWarnings({"PMD.AvoidUsingVolatile", "PMD.NonThreadSafeSingleton"})
public final class Singletone1 {
    private static volatile Singletone1 instance;

    private Singletone1() {
    }

    public static Singletone1 getInstance() {
        if (instance == null) {
            synchronized (Singletone1.class) {
                if (instance == null) {
                    instance = new Singletone1();
                }
            }
        }
        return instance;
    }
}
