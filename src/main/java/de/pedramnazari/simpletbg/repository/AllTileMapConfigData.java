package de.pedramnazari.simpletbg.repository;

import de.pedramnazari.simpletbg.service.TileMapConfig;

import java.util.HashMap;
import java.util.Map;

// TODO: Contains the hard-coded configuration of all maps. Use file/db instead and move to outside layer
// TODO: Is start position of hero per map needed?
public class AllTileMapConfigData {

    private final static int[][] map1 = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    };

    private final static int[][] map2 = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 11, 0, 0, 0, 11, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 11, 0, 1},
    };

    private static final Map<String, TileMapConfig> allMaps;


    private AllTileMapConfigData() {
    }

    static {
        allMaps = new HashMap<>();

        allMaps.put("1", new TileMapConfig("1", map1));
        allMaps.put("2", new TileMapConfig("2", map2));
    }

    public static TileMapConfig getMapConfig(String mapID) {
        if (!allMaps.containsKey(mapID)) {
            throw new IllegalArgumentException("Invalid map ID: " + mapID);
        }

        return allMaps.get(mapID);
    }
}
