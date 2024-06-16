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
        return 0;
    }

    public int getY() {
        return 0;
    }

    public int getType() {
        return type;
    }
}
