package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.Collection;

public class GameContext {

    private final TileMap tileMap;
    private final ItemService itemService;
    private final HeroService heroService;
    private final EnemyService enemyService;
    private final String currentMapIndex;


    // TODO: move to appropriate package
    public GameContext(TileMap tileMap, ItemService itemService, HeroService heroService, EnemyService enemyService, String currentMapIndex) {
        this.tileMap = tileMap;
        this.itemService = itemService;
        this.heroService = heroService;
        this.enemyService = enemyService;
        this.currentMapIndex = currentMapIndex;
    }


    public TileMap getTileMap() {
        return tileMap;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public IHero getHero() {
        return heroService.getHero();
    }

    public HeroService getHeroService() {
        return heroService;
    }

    public EnemyService getEnemyService() {
        return enemyService;
    }

    public Collection<IEnemy> getEnemies() {
        return enemyService.getEnemies();
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }
}
