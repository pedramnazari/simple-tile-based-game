package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.Collection;

// TODO: delete this class? at least it should not be a singleton.
public class GameContext {

    private static volatile GameContext instance;

    private final TileMap tileMap;
    private final IItemService itemService;
    private final IHeroService heroService;
    private final EnemyService enemyService;
    private final String currentMapIndex;

    private GameContext(TileMap tileMap, IItemService itemService, IHeroService heroService, EnemyService enemyService, String currentMapIndex) {
        this.tileMap = tileMap;
        this.itemService = itemService;
        this.heroService = heroService;
        this.enemyService = enemyService;
        this.currentMapIndex = currentMapIndex;
    }

    public static GameContext getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GameContext is not initialized. Call initialize() first.");
        }
        return instance;
    }

    public static synchronized void initialize(TileMap tileMap, IItemService itemService, IHeroService heroService, EnemyService enemyService, String currentMapIndex) {
        if (instance == null) {
            instance = new GameContext(tileMap, itemService, heroService, enemyService, currentMapIndex);
        } else {
            throw new IllegalStateException("GameContext is already initialized.");
        }
    }

    public static synchronized void resetInstance() {
        instance = null;
    }


    public TileMap getTileMap() {
        return tileMap;
    }

    public IItemService getItemService() {
        return itemService;
    }

    public IHero getHero() {
        return heroService.getHero();
    }

    public IHeroService getHeroService() {
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
