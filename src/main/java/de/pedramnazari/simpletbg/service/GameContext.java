package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.MapNavigator;
import de.pedramnazari.simpletbg.model.TileMap;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GameContext {

    private final TileMap tileMap;
    private final IItemService itemService;
    private final Hero hero;
    private final Collection<Enemy> enemies;
    private final String currentMapIndex;
    private MapNavigator mapNavigator;


    public GameContext(TileMap tileMap, IItemService itemService, Hero hero, Collection<Enemy> enemies, String currentMapIndex) {
        this.tileMap = tileMap;
        this.itemService = itemService;
        this.hero = hero;
        this.enemies = enemies;
        this.currentMapIndex = currentMapIndex;
    }


    public TileMap getTileMap() {
        return tileMap;
    }

    public IItemService getItemService() {
        return itemService;
    }

    public Hero getHero() {
        return hero;
    }

    public Collection<Enemy> getEnemies() {
        return List.copyOf(enemies);
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }

    public void setMapNavigator(MapNavigator mapNavigator) {
        this.mapNavigator = mapNavigator;
    }

    public Optional<MapNavigator> getMapNavigator() {
        return Optional.ofNullable(mapNavigator);
    }
}
