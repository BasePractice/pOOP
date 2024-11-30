package ru.mifi.practice.vol7;

public abstract class Main {

    public static void main(String[] args) {
        new Destructor();
    }

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    private static final class Destructor {
        public Destructor() {
            try {
                finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
