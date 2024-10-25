package ru.mifi.practice.vol6;

import java.io.PrintStream;

public interface Output {
    Output DEFAULT = new Standard();

    void print(String format, Object... args);

    default void println(String format, Object... args) {
        print(format + "%n", args);
    }

    void error(String format, Object... args);

    default void errorln(String format, Object... args) {
        error(format + "%n", args);
    }


    final class Standard implements Output {
        private final PrintStream output;
        private final PrintStream error;

        private Standard() {
            output = System.out;
            error = System.err;
        }

        @Override
        public void print(String format, Object... args) {
            output.printf(format, args);
            output.flush();
        }

        @Override
        public void error(String format, Object... args) {
            error.printf(format, args);
            error.flush();
        }
    }
}
