package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnemyServiceTest {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY.getType();
    private static final int W = TileType.WALL.getType();
    private static final int F = TileType.FLOOR.getType();

    private EnemyService enemyService;
    private EnemyMovementService enemyMovementService;

    @BeforeEach
    public void setUp() {
        enemyMovementService = new EnemyMovementService();
        enemyService = new EnemyService(new DefaultEnemyFactory(), enemyMovementService);
    }

    @Test
    public void testCreateEnemies() {
        int[][] map = new int[][] {
                {O, O, E},
                {E, O, O},
                {O, E, O},
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
        final int[][] enemyMapArray = new int[][] {
                {O, E, O},
                {O, O, O},
                {O, O, O},
        };

        final int[][] tileMapArray = new int[][] {
                {F, F, W},
                {W, F, W},
                {W, F, F},
        };

        final TileMap tileMap = new TileMap(new DefaultTileFactory(new DefaultItemFactory()), "map", tileMapArray);

        final Collection<Enemy> enemies = enemyService.createEnemies(new TileMapConfig("enemyMap", enemyMapArray));
        assertNotNull(enemies);
        assertEquals(1, enemies.size());

        final Enemy aEnemy = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

        for (int i = 0; i < 20; i++) {
            final List<MovementResult> results = enemyService.moveEnemiesRandomlyWithinMap(enemies, tileMap, List.of());
            assertEquals(enemies.size(), results.size());

            // Since the enemies move randomly (so we do not know the target position),
            // check again that all moves were valid.
            for (MovementResult result : results) {
                enemyMovementService
                        .isValidMovePositionWithinMap(tileMap, result.getOldX(), result.getOldY(), result.getNewX(), result.getNewY());
            }
        }


    }

}
