package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;

public class Item implements ITileMapElement {
    private final int x;
    private final int y;
    private final String name;
    private final String description;
    // TODO: move to ITileMapElement and change type to enum
    private final int type;

    public Item(int x, int y, String name, String description, int type) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.description = description;
        this.type = type;
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

    public int getType() {
        return type;
    }
}
