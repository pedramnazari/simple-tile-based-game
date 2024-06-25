package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.MoveDirections;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.service.MovementResult;
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

    public TileMap startGameUsingMap(TileMapConfig mapConfig, TileMapConfig itemConfig) {
        return tileMapService.createAndInitMap(mapConfig, itemConfig);
    }

    public MovementResult moveHeroToRight() {
        return tileMapService.moveHero(MoveDirections.RIGHT);
    }

    public MovementResult moveHeroToLeft() {
        return tileMapService.moveHero(MoveDirections.LEFT);
    }

    public MovementResult moveHeroUp() {
        return tileMapService.moveHero(MoveDirections.UP);
    }

    public MovementResult moveHeroDown() {
        return tileMapService.moveHero(MoveDirections.DOWN);
    }

    public TileMap getItemMap() {
        return tileMapService.getItemMap();
    }


    public TileMap getTileMap() {
        return tileMapService.getTileMap();
    }

    public Hero getHero() {
        return tileMapService.getHero();
    }
}
