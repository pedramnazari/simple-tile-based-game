package de.pedramnazari.simpletbg.model;

public class Tile {


    private final int type;
    private final int x;
    private final int y;

    public Tile(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }
}
