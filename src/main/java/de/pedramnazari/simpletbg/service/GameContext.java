package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.Collection;
import java.util.List;

public class GameContext {

    private final TileMap tileMap;
    private final ItemService itemService;
    private final IHero hero;
    private final Collection<Enemy> enemies;
    private final String currentMapIndex;


    // TODO: move to appropriate package
    public GameContext(TileMap tileMap, ItemService itemService, IHero hero, Collection<Enemy> enemies, String currentMapIndex) {
        this.tileMap = tileMap;
        this.itemService = itemService;
        this.hero = hero;
        this.enemies = enemies;
        this.currentMapIndex = currentMapIndex;
    }


    public TileMap getTileMap() {
        return tileMap;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public IHero getHero() {
        return hero;
    }

    public Collection<Enemy> getEnemies() {
        return List.copyOf(enemies);
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }
}
