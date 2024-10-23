package ru.mifi.practice.val3.poly;

public abstract class Main {
    public static void main(String[] args) {
        BusinessComponent component = DaggerBusinessComponent.create();
        component.businessLogic().execute();
    }
}
