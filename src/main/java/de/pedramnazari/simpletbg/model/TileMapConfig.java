package de.pedramnazari.simpletbg.model;

import java.util.Arrays;

public class TileMapConfig {

    private final String mapId;
    private final int[][] map;

    public TileMapConfig(String mapId, int[][] map) {
        this.mapId = mapId;
        this.map = map;
    }

    public String getMapId() {
        return mapId;
    }

    public int[][] getMap() {
        return Arrays.copyOf(map, map.length);
    }
}
