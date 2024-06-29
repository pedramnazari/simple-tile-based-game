package de.pedramnazari.simpletbg.model;

public enum AllTileType {

    EMPTY("_0"),

    // "normal" tiles (e.g., floor)
    FLOOR("t1"),

    // obstacles (e.g., walls)
    WALL("o1"),

    // the hero
    HERO("h1"),

    // enemies
    ENEMY1("e1"), E1("e1"),
    ENEMY2("e2"), E2("e2");


    private final String type;

    AllTileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static AllTileType fromType(String type) {
        for (AllTileType tileTypeEnum : values()) {
            if (tileTypeEnum.type.equals(type)) {
                return tileTypeEnum;
            }
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
