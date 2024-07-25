package de.pedramnazari.simpletbg.tilemap.model;

// TODO: split to multiple sub-classes (e.g.DestructibleTile, DoorTile, etc.)
public class Tile implements ITileMapElement {
    private final int type;
    private final int x;
    private final int y;
    private boolean isObstacle;
    private boolean isDestructible;
    private int transformToNewTileType = -1;

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

    public boolean isDestructible() {
        return isDestructible;
    }

    public void setDestructible(boolean destructible) {
        isDestructible = destructible;
    }

    public void hit() {
        if (this.isDestructible && !this.isDestroyed()) {
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

    public int getTransformToNewTileType() {
        return transformToNewTileType;
    }

    public void setTransformToNewTileType(int transformToNewTileType) {
        this.transformToNewTileType = transformToNewTileType;
    }

    public boolean canTransformToNewTileType() {
        return this.transformToNewTileType != -1;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", isObstacle=" + isObstacle +
                ", isDestroyable=" + isDestructible +
                ", hitPoints=" + hitPoints +
                '}';
    }
}
