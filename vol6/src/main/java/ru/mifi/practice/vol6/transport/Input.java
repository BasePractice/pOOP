package ru.mifi.practice.vol6.transport;

import java.util.Optional;
import java.util.Scanner;

public interface Input extends AutoCloseable {
    Input DEFAULT = new Standard();

    String inputString();

    Optional<Number> inputNumber();

    final class Standard implements Input {
        private final Scanner scanner;

        private Standard() {
            scanner = new Scanner(System.in);
        }

        @Override
        public void close() {
            scanner.close();
        }

        @Override
        public String inputString() {
            return scanner.nextLine();
        }

        @Override
        public Optional<Number> inputNumber() {
            String input = inputString();
            try {
                return Optional.of(Double.parseDouble(input));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
    }
}
