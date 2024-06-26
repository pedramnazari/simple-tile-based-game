package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.model.TileMapTestHelper;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyServiceTest {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY.getType();
    private static final int W = TileType.WALL.getType();
    private static final int F = TileType.FLOOR.getType();

    private EnemyService enemyService;
    private EnemyMovementService enemyMovementService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testInit() {
        int[][] map = new int[][]{
                {O, O, E},
                {E, O, O},
                {O, E, O},
        };

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        enemyMovementService = new EnemyMovementService(new RandomMovementStrategy(collisionDetectionService), collisionDetectionService);

        final DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory();
        enemyService = new EnemyService(enemyFactory, enemyMovementService);

        assertFalse(enemyService.isInitialized());
        enemyService.init(new EnemyConfigParser().parse(map, enemyFactory));
        assertTrue(enemyService.isInitialized());

        final Collection<Enemy> enemies = enemyService.getEnemies();

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
        CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        RandomMovementStrategy randomMovementStrategy = new RandomMovementStrategy(collisionDetectionService);
        enemyMovementService = new EnemyMovementService(randomMovementStrategy, collisionDetectionService);
        enemyService = new EnemyService(new DefaultEnemyFactory(), enemyMovementService);

        final int[][] enemyMapArray = new int[][]{
                {O, E, O},
                {O, O, O},
                {O, O, O},
        };

        final int[][] tileMapArray = new int[][]{
                {F, F, W},
                {W, F, W},
                {W, F, F},
        };

        final TileMap tileMap = TileMapTestHelper.createMapUsingDefaults("map", tileMapArray);

        enemyService.init(new EnemyConfigParser().parse(enemyMapArray, new DefaultEnemyFactory()));
        final Collection<Enemy> enemies = enemyService.getEnemies();

        assertNotNull(enemies);
        assertEquals(1, enemies.size());

        final Enemy aEnemy = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

        final GameContext gameContext = new GameContextBuilder()
                .setTileMap(tileMap)
                .setEnemies(enemies)
                .setHero(new Hero(0, 0))
                .setItemService(new ItemServiceMock())
                .build();

        for (int i = 0; i < 20; i++) {
            final List<MovementResult> results = enemyService.moveEnemiesRandomlyWithinMap(gameContext);
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
