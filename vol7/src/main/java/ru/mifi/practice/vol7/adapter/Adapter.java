package ru.mifi.practice.vol7.adapter;

public final class Adapter implements Int2 {

    private final Int1 origin;

    public Adapter(Int1 origin) {
        this.origin = origin;
    }

    @Override
    public String getProcess() {
        return origin.getName();
    }
}
