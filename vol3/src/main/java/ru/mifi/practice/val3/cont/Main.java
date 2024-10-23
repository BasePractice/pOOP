package ru.mifi.practice.val3.cont;

import ru.mifi.practice.val3.cont.business.DefaultLogic;

public abstract class Main {
    public static void main(String[] args) {
        BusinessLogic logic = new DefaultLogic.Default();
        logic.execute();
    }
}
