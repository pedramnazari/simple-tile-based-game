package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.MoveDirections;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.TileMapService;

public class TileMapController {

    private final TileMapService tileMapService;


    public TileMapController(final TileMapService tileMapService) {
        this.tileMapService = tileMapService;
    }

    public TileMap startNewGame() {
        return this.startGameUsingMap(AllTileMapConfigData.getMapConfig("1"));
    }

    public TileMap startGameUsingMap(TileMapConfig mapConfig) {
        return tileMapService.createAndInitMap(mapConfig);
    }

    public void moveHeroToRight() {
        tileMapService.moveHero(MoveDirections.RIGHT);
    }

    public void moveHeroToLeft() {
        tileMapService.moveHero(MoveDirections.LEFT);
    }

    public void moveHeroUp() {
        tileMapService.moveHero(MoveDirections.UP);
    }

    public void moveHeroDown() {
        tileMapService.moveHero(MoveDirections.DOWN);
    }


}
