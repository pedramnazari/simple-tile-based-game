package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.Item;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.service.MovementResult;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.TileMapService;

import java.util.Collection;

public class TileMapController {

    private final TileMapService tileMapService;


    public TileMapController(final TileMapService tileMapService) {
        this.tileMapService = tileMapService;
    }

    public TileMap startNewGame() {
        return this.startGameUsingMap(AllTileMapConfigData.getMapConfig("1"), 1, 0);
    }

    public TileMap startGameUsingMap(TileMapConfig mapConfig, int heroX, int heroY) {
        return tileMapService.createAndInitMap(mapConfig, heroX, heroY);
    }

    public TileMap startGameUsingMap(TileMapConfig mapConfig, TileMapConfig itemConfig, int heroX, int heroY) {
        return tileMapService.createAndInitMap(mapConfig, itemConfig, heroX, heroY);
    }

    public MovementResult moveHeroToRight() {
        return tileMapService.moveHeroToRight();
    }

    public MovementResult moveHeroToLeft() {
        return tileMapService.moveHeroToLeft();
    }

    public MovementResult moveHeroUp() {
        return tileMapService.moveHeroUp();
    }

    public MovementResult moveHeroDown() {
        return tileMapService.moveHeroDown();
    }

    public Collection<Item> getItems() {
        return tileMapService.getItems();
    }


    public TileMap getTileMap() {
        return tileMapService.getTileMap();
    }

    public Hero getHero() {
        return tileMapService.getHero();
    }
}
