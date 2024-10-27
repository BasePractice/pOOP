package ru.mifi.practice.vol7.factory;

public interface EmployeeFactory {

    Employee createEmployee(String name);

    Director createDirector(String name);
}
