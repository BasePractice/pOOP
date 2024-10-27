package ru.mifi.practice.vol7.factory.impl;

import ru.mifi.practice.vol7.factory.Director;
import ru.mifi.practice.vol7.factory.Employee;
import ru.mifi.practice.vol7.factory.EmployeeFactory;

public final class ConcreteEmployeeFactory implements EmployeeFactory {
    @Override
    public Employee createEmployee(String name) {
        return null;
    }

    @Override
    public Director createDirector(String name) {
        return null;
    }
}
