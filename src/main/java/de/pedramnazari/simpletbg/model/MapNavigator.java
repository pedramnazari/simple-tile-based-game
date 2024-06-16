package de.pedramnazari.simpletbg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapNavigator {
    private final Map<Integer, Map<MoveDirections, Integer>> mapConnections = new HashMap<>();

    private final List<int[][]> maps;

    public MapNavigator() {
        this.maps = new ArrayList<>();
    }

    public void addMap(final int[][] map) {
        this.maps.add(map);
    }

    public void addConnection(int fromMapId, MoveDirections direction, int toMapId) {
        mapConnections.putIfAbsent(fromMapId, new HashMap<>());
        mapConnections.get(fromMapId).put(direction, toMapId);
    }

    public int getNextMapId(int currentMapId, MoveDirections direction) {
        return mapConnections.getOrDefault(currentMapId, new HashMap<>()).getOrDefault(direction, currentMapId);
    }

    public int[][] getMap(int mapIndex) {
        return maps.get(mapIndex);
    }
}
