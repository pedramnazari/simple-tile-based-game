package de.pedramnazari.simpletbg.tile.model;

public enum TileType {

    EMPTY(0),
    FLOOR(1),
    WALL(20),
    HERO(1000),
    ENEMY(500),
    ITEM(200),
    EXIT(400),
    ENEMY_EXIT(600);

    private final int type;

    TileType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static TileType fromType(int type) {
        for (TileType tileTypeEnum : values()) {
            if (tileTypeEnum.type == type) {
                return tileTypeEnum;
            }
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
