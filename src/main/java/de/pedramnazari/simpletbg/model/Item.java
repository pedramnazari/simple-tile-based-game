package de.pedramnazari.simpletbg.model;

public class Item implements ITileMapElement {
    private final int x;
    private final int y;
    private final String name;
    private final String description;

    public Item(int x, int y, String name, String description) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
