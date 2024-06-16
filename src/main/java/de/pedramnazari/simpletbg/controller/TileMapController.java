package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.service.TileMapService;

public class TileMapController {

    private final TileMapService tileMapService;


    public TileMapController(final TileMapService tileMapService) {
        this.tileMapService = tileMapService;
    }

    public TileMap startNewGame() {
        return this.startGameUsingMap(TileMapConfig.getMapConfig("1"));
    }

    public TileMap startGameUsingMap(int[][] mapConfig) {
        final TileMap tileMap = tileMapService.createAndInitMap(mapConfig);
        return tileMap;
    }
}
