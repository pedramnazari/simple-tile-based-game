package de.pedramnazari.simpletbg.model;

public class Tile implements ITileMapElement {
    private final int type;
    private final int x;
    private final int y;
    private boolean isObstacle;
    private Item item;

    public Tile(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.item = null;
    }

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

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    public boolean isItem() {
        return this.item != null;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }
}
