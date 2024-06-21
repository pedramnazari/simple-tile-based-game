package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.model.TileMapConfig;
import de.pedramnazari.simpletbg.service.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.TileMapService;

public class TileMapController {

    private final TileMapService tileMapService;


    public TileMapController(final TileMapService tileMapService) {
        this.tileMapService = tileMapService;
    }

    public TileMap startNewGame() {
        return this.startGameUsingMap(AllTileMapConfigData.getMapConfig("1"), "1");
    }

    public TileMap startGameUsingMap(TileMapConfig mapConfig, String mapId) {
        return tileMapService.createAndInitMap(mapConfig);
    }
}
