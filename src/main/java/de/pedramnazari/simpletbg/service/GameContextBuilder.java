package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.ArrayList;
import java.util.Collection;

public class GameContextBuilder {
    private TileMap tileMap;
    private ItemService itemService;
    private IHero hero;
    private Collection<IEnemy> enemies = new ArrayList<>();
    private String currentMapIndex;

    public GameContextBuilder setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
        return this;
    }

    public GameContextBuilder setItemService(ItemService itemService) {
        this.itemService = itemService;
        return this;
    }

    public GameContextBuilder setHero(IHero hero) {
        this.hero = hero;
        return this;
    }

    public GameContextBuilder setEnemies(Collection<IEnemy> enemies) {
        this.enemies = new ArrayList<>(enemies);
        return this;
    }

    public GameContextBuilder setCurrentMapIndex(String currentMapIndex) {
        this.currentMapIndex = currentMapIndex;
        return this;
    }

    public GameContext build() {
        enemies = (enemies != null) ? enemies : new ArrayList<>();

        GameContext gameContext = new GameContext(tileMap, itemService, hero, enemies, currentMapIndex);

        return gameContext;
    }
}
