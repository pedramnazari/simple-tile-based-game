package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;
import de.pedramnazari.simpletbg.model.TileMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class EnemyServiceTest {

    private EnemyService enemyService;

    @BeforeEach
    public void setUp() {
        IEnemyFactory enemyFactory = new DefaultEnemyFactory();

        enemyService = new EnemyService(enemyFactory);
    }

    @Test
    public void testCreateAndInitMapWithEnemies() {
        final TileMapConfig enemyMapConfig = new TileMapConfig("e1", new int[][] {
                {0, 0, 1},
                {1, 0, 0},
                {0, 1, 0},
        });

        final Collection<Enemy> enemies = enemyService.createEnemyMap(enemyMapConfig);


    }




}
