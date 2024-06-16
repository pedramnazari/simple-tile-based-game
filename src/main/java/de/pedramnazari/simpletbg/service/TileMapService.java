package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.TileMap;

import java.util.Objects;

public class TileMapService {

    public TileMap createAndInitMap(int[][] mapConfig) {
        Objects.requireNonNull(mapConfig);

        final TileMap tileMap = new TileMap(mapConfig[0].length, mapConfig.length);

        tileMap.load(mapConfig);

        return tileMap;
    }
}
