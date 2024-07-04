package de.pedramnazari.simpletbg.tilemap.model;

public enum TileType {

    EMPTY(0),
    WOOD(1),
    STONE(2),
    FLOOR(3),
    GRASS(4),
    WALL(11),

    ITEM_YELLOW_KEY(100),
    ITEM_YELLOW_KEY2(101),

    WEAPON_SWORD(200),
    WEAPON_SWORD2(201),

    ENEMY_LR(500), // left-right,
    ENEMY_TD(510), // top-down
    ENEMY_2D(520), // two-dimensional

    HERO(1000);


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
