package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.Collection;
import java.util.logging.Logger;

public class EnemyService {
    private final Logger logger = Logger.getLogger(EnemyService.class.getName());

    private final IEnemyFactory enemyFactory;
    private final EnemyMovementService enemyMovementService;

    public EnemyService(IEnemyFactory enemyFactory, EnemyMovementService enemyMovementService) {
        this.enemyFactory = enemyFactory;
        this.enemyMovementService = enemyMovementService;
    }

    public Collection<Enemy> createEnemies(TileMapConfig enemyMapConfig) {
        return enemyFactory.createElementsUsingTileMapConfig(enemyMapConfig);
    }

    public void moveEnemies(Collection<Enemy> enemies, final TileMap tileMap, final Collection<Item> items,
                            final MoveDirection moveDirection, final MapNavigator mapNavigator, final String currentMapIndex) {
        for (Enemy enemy : enemies) {
            enemyMovementService.moveTileMapElement(tileMap, items, enemy, moveDirection, mapNavigator, currentMapIndex);
        }
    }


}
