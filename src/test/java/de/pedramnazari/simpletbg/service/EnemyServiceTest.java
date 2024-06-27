package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnemyServiceTest {

    private EnemyService enemyService;
    private static final int E = TileType.ENEMY.getType();

    @BeforeEach
    public void setUp() {
        enemyService = new EnemyService(new DefaultEnemyFactory(), new EnemyMovementService());
    }

    @Test
    public void testCreateEnemies() {
        int[][] map = new int[][] {
                {0, 0, E},
                {E, 0, 0},
                {0, E, 0},
        };

        final Collection<Enemy> enemies = enemyService.createEnemies(new TileMapConfig("map1", map));
        assertNotNull(enemies);
        assertEquals(3, enemies.size());

        for (Enemy enemy : enemies) {
            assertNotNull(enemy);
        }

        final Enemy aEnemy = enemies.stream().filter(e -> e.getX() == 2 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

        final Enemy bEnemy = enemies.stream().filter(e -> e.getX() == 0 && e.getY() == 1).findFirst().orElse(null);
        assertNotNull(bEnemy);

        final Enemy cEnemy = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 2).findFirst().orElse(null);
        assertNotNull(cEnemy);
    }

    @Test
    public void testMoveEnemies() {
        int[][] map = new int[][] {
                {0, 0, E},
                {0, 0, 0},
                {0, 0, 0},
        };

        final Collection<Enemy> enemies = enemyService.createEnemies(new TileMapConfig("map1", map));
        assertNotNull(enemies);
        assertEquals(1, enemies.size());

        for (Enemy enemy : enemies) {
            assertNotNull(enemy);
        }

        final Enemy aEnemy = enemies.stream().filter(e -> e.getX() == 2 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

    }

}
