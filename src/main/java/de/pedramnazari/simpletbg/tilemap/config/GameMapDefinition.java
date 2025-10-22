package de.pedramnazari.simpletbg.tilemap.config;

import java.util.Objects;

public final class GameMapDefinition {

    private final String id;
    private final String displayName;
    private final int[][] map;
    private final int[][] items;
    private final int[][] enemies;
    private final int heroStartColumn;
    private final int heroStartRow;

    public GameMapDefinition(String id,
                              String displayName,
                              int[][] map,
                              int[][] items,
                              int[][] enemies,
                              int heroStartColumn,
                              int heroStartRow) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
        this.map = Objects.requireNonNull(map, "map must not be null");
        this.items = Objects.requireNonNull(items, "items must not be null");
        this.enemies = Objects.requireNonNull(enemies, "enemies must not be null");
        this.heroStartColumn = heroStartColumn;
        this.heroStartRow = heroStartRow;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int[][] getMap() {
        return map;
    }

    public int[][] getItems() {
        return items;
    }

    public int[][] getEnemies() {
        return enemies;
    }

    public int getHeroStartColumn() {
        return heroStartColumn;
    }

    public int getHeroStartRow() {
        return heroStartRow;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
