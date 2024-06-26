package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;

public class TileMapService {

    private final ITileFactory tileFactory;
    private final IItemFactory itemFactory;
    private final MovementService movementService;
    private final Hero hero;
    private MapNavigator mapNavigator;
    private String currentMapIndex;

    // Maps
    private TileMap tileMap;
    private Collection<Item> items;
    private TileMap enemyMap;

    public TileMapService(ITileFactory tileFactory, IItemFactory itemFactory, MovementService movementService, final Hero hero) {
        this.tileFactory = tileFactory;
        this.itemFactory = itemFactory;
        this.movementService = movementService;
        this.hero = hero;
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, TileMapConfig itemConfig) {
        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        this.items = itemFactory.createItemsUsingTileMapConfig(itemConfig);
        return this.createAndInitMap(mapConfig);
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig) {
        Objects.requireNonNull(mapConfig);

        // TODO: use factory to create map
        this.tileMap = new TileMap(tileFactory, mapConfig.getMapId(), mapConfig.getMap());

        return tileMap;
    }

    public TileMap createAndInitMap(MapNavigator mapNavigator, final String idOfStartingMap) {
        this.mapNavigator = mapNavigator;
        this.currentMapIndex = idOfStartingMap;

        this.tileMap = mapNavigator.getMap(idOfStartingMap);

        return tileMap;
    }

    public MovementResult moveHero(MoveDirections moveDirections) {
        final MovementResult result = movementService.moveTileMapElement(tileMap, items, hero, moveDirections, mapNavigator, currentMapIndex);

        if(result.hasMoved()) {
            currentMapIndex = result.getNewMapIndex();
        }

        return result;
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }

    public Collection<Item> getItems() {
        return List.copyOf(items);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Hero getHero() {
        return hero;
    }


}
