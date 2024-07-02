package de.pedramnazari.simpletbg.tilemap.model;

public enum TileType {

    EMPTY(0),
    FLOOR(1),
    WALL(20),
    HERO(1000),
    ENEMY_LR(500), // left-right
    ENEMY_TD(510), // top-down
    ENEMY_2D(520), // two-dimensional
    ITEM(200),
    EXIT(400);

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
