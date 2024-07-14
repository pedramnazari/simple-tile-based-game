package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

public class GameContextBuilder {
    private TileMap tileMap;
    private ItemService itemService;
    private String currentMapIndex;
    private HeroService heroService;
    private EnemyService enemyService;

    public GameContextBuilder setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
        return this;
    }

    public GameContextBuilder setItemService(ItemService itemService) {
        this.itemService = itemService;
        return this;
    }

    public GameContextBuilder setCurrentMapIndex(String currentMapIndex) {
        this.currentMapIndex = currentMapIndex;
        return this;
    }

    public GameContextBuilder setHeroService(HeroService heroService) {
        this.heroService = heroService;
        return this;
    }

    public GameContextBuilder setEnemyService(EnemyService enemyService) {
        this.enemyService = enemyService;
        return this;
    }

    public GameContext build() {
        GameContext gameContext = new GameContext(tileMap, itemService, heroService, enemyService, currentMapIndex);

        return gameContext;
    }
}
