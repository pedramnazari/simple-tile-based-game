package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IItem;

public class Item implements IItem {
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

    @Override
    public String getName() {
        return name;
    }

    @Override
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

    @Override
    public int getType() {
        return type;
    }
}
