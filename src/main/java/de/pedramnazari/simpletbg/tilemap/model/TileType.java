package de.pedramnazari.simpletbg.tilemap.model;

public enum TileType {

    EMPTY(0),
    WOOD(1),
    STONE(2),
    FLOOR1(3),
    FLOOR2(7),
    GRASS(4),
    PATH(5),
    GRASS_WITH_STONES(6),
    WALL(11),

    ITEM_YELLOW_KEY(100),
    ITEM_YELLOW_KEY2(101),

    WEAPON_SWORD(200),
    WEAPON_SWORD2(201),
    WEAPON_LANCE(220),
    WEAPON_DOUBLE_ENDED_LANCE(221), // attacks foward and backward
    WEAPON_MULTI_SPIKE_LANCE(222), // attacks in all directions

    WEAPON_BOMB_PLACER(230),
    WEAPON_BOMB(231),

    RING_MAGIC1(300),

    ENEMY_LR(500), // left-right,
    ENEMY_TD(510), // top-down
    ENEMY_2D(520), // two-dimensional
    ENEMY_FH(530), // follow hero

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
