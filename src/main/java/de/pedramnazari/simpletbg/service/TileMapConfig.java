package de.pedramnazari.simpletbg.service;

import java.util.HashMap;
import java.util.Map;

// TODO: Contains the hard-coded configuration of all maps. Use file/db instead and move to outside layer
public class TileMapConfig {

    private final static int[][] map1 = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 6},
    };

    private static final Map<String, int[][]> allMaps;

    static {
        allMaps = new HashMap<>();
        allMaps.put("1", map1);
    }

    public static int[][] getMapConfig(String mapID) {
        if (!allMaps.containsKey(mapID)) {
            throw new IllegalArgumentException("Invalid map ID: " + mapID);
        }

        return allMaps.get(mapID);
    }
}
