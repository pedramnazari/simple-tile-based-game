package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.inventory.service.IItemService;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.Collection;
import java.util.List;

public class GameContext {

    private final TileMap tileMap;
    private final IItemService itemService;
    private final Hero hero;
    private final Collection<Enemy> enemies;
    private final String currentMapIndex;


    // TODO: move to appropriate package
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
}
