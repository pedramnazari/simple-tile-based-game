package de.pedramnazari.simpletbg.service;

import java.util.HashMap;
import java.util.Map;

// TODO: Contains the hard-coded configuration of all maps. Use file/db instead
public class TileMapConfig {

    private final static int[][] map1Config = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 6},
    };

    private static final Map<String, int[][]> allMapConfigs;

    static {
         allMapConfigs = new HashMap<String, int[][]>();
         allMapConfigs.put("1", map1Config);
    }

    public static int[][] getMapConfig(String mapID) {
        if (!allMapConfigs.containsKey(mapID)) {
            throw new IllegalArgumentException("Invalid map ID: " + mapID);
        }

        return allMapConfigs.get(mapID);
    }
}
