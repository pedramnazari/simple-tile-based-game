package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;

public class TileMapService {

    private final ITileFactory tileFactory;
    private final IItemFactory itemFactory;
    private final HeroService heroService;
    private Hero hero;
    private MapNavigator mapNavigator;
    private String currentMapIndex;

    // TODO: Introduce GameWorld class to hold all maps, items, enemies, hero etc.
    // Maps
    private TileMap tileMap;
    private Collection<Item> items = new ArrayList<>();
    private final Collection<Enemy> enemies = new ArrayList<>();

    public TileMapService(ITileFactory tileFactory, IItemFactory itemFactory, HeroService heroService) {
        this.tileFactory = tileFactory;
        this.itemFactory = itemFactory;
        this.heroService = heroService;
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, TileMapConfig itemConfig, int heroX, int heroY) {
        this.hero = heroService.createHero(heroX, heroY);
        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        this.items = itemFactory.createElementsUsingTileMapConfig(itemConfig);
        return this.createAndInitMap(mapConfig, heroX, heroY);
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, int heroX, int heroY) {
        Objects.requireNonNull(mapConfig);

        // TODO: check whether hero position is valid

        this.hero = heroService.createHero(heroX, heroY);

        // TODO: use factory to create map
        this.tileMap = new TileMap(tileFactory, mapConfig.getMapId(), mapConfig.getMap());

        return tileMap;
    }

    public TileMap createAndInitMap(MapNavigator mapNavigator, final String idOfStartingMap, int heroX, int heroY) {
        this.mapNavigator = mapNavigator;
        this.hero = heroService.createHero(heroX, heroY);

        this.currentMapIndex = idOfStartingMap;

        this.tileMap = mapNavigator.getMap(idOfStartingMap);

        return tileMap;
    }

    public MovementResult moveHeroToLeft() {
        return moveHero(MoveDirection.LEFT);
    }

    public MovementResult moveHeroToRight() {
        return moveHero(MoveDirection.RIGHT);
    }

    public MovementResult moveHeroUp() {
        return moveHero(MoveDirection.UP);
    }

    public MovementResult moveHeroDown() {
        return moveHero(MoveDirection.DOWN);
    }

    protected MovementResult moveHero(MoveDirection moveDirection) {
        final MovementResult result = heroService.moveTileMapElement(tileMap, items, hero, moveDirection, mapNavigator, currentMapIndex);

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
