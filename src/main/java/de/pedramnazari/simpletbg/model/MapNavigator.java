package de.pedramnazari.simpletbg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapNavigator {
    private final Map<String, Map<MoveDirections, String>> mapConnections = new HashMap<>();

    private final Map<String, int[][]> maps;

    public MapNavigator() {
        this.maps = new HashMap<>();
    }

    public void addMap(final int[][] map, String mapId) {
        if (this.maps.containsKey(mapId)) {
            throw new IllegalArgumentException("Map with id " + mapId + " already exists");
        }

        this.maps.put(mapId, map);
    }

    public void addConnection(String fromMapId, MoveDirections direction, String toMapId) {
        mapConnections.putIfAbsent(fromMapId, new HashMap<>());
        mapConnections.get(fromMapId).put(direction, toMapId);
    }

    public String getNextMapId(String currentMapId, MoveDirections direction) {
        return mapConnections.getOrDefault(currentMapId, new HashMap<>()).getOrDefault(direction, currentMapId);
    }

    public int[][] getMap(String mapIndex) {
        return maps.get(mapIndex);
    }
}
