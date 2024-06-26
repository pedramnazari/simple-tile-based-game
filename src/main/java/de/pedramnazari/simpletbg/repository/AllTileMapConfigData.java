package de.pedramnazari.simpletbg.repository;

import de.pedramnazari.simpletbg.service.TileMapConfig;

import java.util.HashMap;
import java.util.Map;

// TODO: Contains the hard-coded configuration of all maps. Use file/db instead and move to outside layer
// TODO: Is start position of hero per map needed?
public class AllTileMapConfigData {

    private final static int[][] map1 = {
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
    };


    private final static int[][] map2 = {
            {3, 1, 11, 1, 1, 1, 1, 1, 1, 3},
            {1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 1, 11, 1, 1, 1, 11, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {3, 1, 11, 1, 1, 1, 1, 11, 1, 3},
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
