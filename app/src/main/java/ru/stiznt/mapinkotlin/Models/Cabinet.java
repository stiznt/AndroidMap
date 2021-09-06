package ru.stiznt.mapinkotlin.Models;

public class Cabinet {
    private String name;
    private int id;

    public Cabinet(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
