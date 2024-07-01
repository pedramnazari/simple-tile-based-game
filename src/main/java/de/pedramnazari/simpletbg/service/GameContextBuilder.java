package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.inventory.service.IItemService;
import de.pedramnazari.simpletbg.model.MapNavigator;
import de.pedramnazari.simpletbg.tile.model.TileMap;

import java.util.ArrayList;
import java.util.Collection;

public class GameContextBuilder {
    private TileMap tileMap;
    private IItemService itemService;
    private Hero hero;
    private Collection<Enemy> enemies = new ArrayList<>();
    private String currentMapIndex;
    private MapNavigator mapNavigator;

    public GameContextBuilder setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
        return this;
    }

    public GameContextBuilder setItemService(IItemService itemService) {
        this.itemService = itemService;
        return this;
    }

    public GameContextBuilder setHero(Hero hero) {
        this.hero = hero;
        return this;
    }

    public GameContextBuilder setEnemies(Collection<Enemy> enemies) {
        this.enemies = new ArrayList<>(enemies);
        return this;
    }

    public GameContextBuilder setCurrentMapIndex(String currentMapIndex) {
        this.currentMapIndex = currentMapIndex;
        return this;
    }

    public GameContextBuilder setMapNavigator(MapNavigator mapNavigator) {
        this.mapNavigator = mapNavigator;
        return this;
    }

    public GameContext build() {
        enemies = (enemies != null) ? enemies : new ArrayList<>();

        GameContext gameContext = new GameContext(tileMap, itemService, hero, enemies, currentMapIndex);
        gameContext.setMapNavigator(mapNavigator);

        return gameContext;
    }
}
