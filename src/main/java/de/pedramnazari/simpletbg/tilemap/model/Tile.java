package de.pedramnazari.simpletbg.tilemap.model;

public class Tile implements ITileMapElement {
    private final int type;
    private final int x;
    private final int y;
    private boolean isObstacle;
    private boolean isDestroyable;

    private int hitPoints = -1;

    public Tile(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
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

    public boolean isDestroyable() {
        return isDestroyable;
    }

    public void setDestroyable(boolean destroyable) {
        isDestroyable = destroyable;
    }

    public void hit() {
        if (this.isDestroyable && !this.isDestroyed()) {
            this.hitPoints--;
        }
    }

    public boolean isDestroyed() {
        return this.hitPoints == 0;
    }

    public boolean isFloor() {
        return !isObstacle();
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", isObstacle=" + isObstacle +
                ", isDestroyable=" + isDestroyable +
                ", hitPoints=" + hitPoints +
                '}';
    }
}
