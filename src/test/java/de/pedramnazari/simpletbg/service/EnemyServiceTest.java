package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.IHeroProvider;
import de.pedramnazari.simpletbg.model.TileMapTestHelper;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyServiceTest {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY_LR.getType();
    private static final int W = TileType.WALL.getType();
    private static final int F = TileType.FLOOR.getType();

    private IEnemyService enemyService;
    private EnemyMovementService enemyMovementService;

    @BeforeEach
    public void setUp() {
        GameContext.resetInstance();
    }

    @Test
    public void testInit() {
        int[][] map = new int[][]{
                {O, O, E},
                {E, O, O},
                {O, E, O},
        };

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyMovementService.addMovementStrategy(new RandomMovementStrategy(collisionDetectionService));
        final IHeroProvider heroProvider = new IHeroProviderMock();

        final DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory(collisionDetectionService, heroProvider);
        enemyService = new EnemyService(enemyMovementService);

        assertFalse(enemyService.isInitialized());
        enemyService.init(new EnemyConfigParser().parse(map, enemyFactory));
        assertTrue(enemyService.isInitialized());

        final Collection<IEnemy> enemies = enemyService.getEnemies();

        assertNotNull(enemies);
        assertEquals(3, enemies.size());

        for (IEnemy enemy : enemies) {
            assertNotNull(enemy);
        }

        final IEnemy aEnemy = enemies.stream().filter(e -> e.getX() == 2 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

        final IEnemy bEnemy = enemies.stream().filter(e -> e.getX() == 0 && e.getY() == 1).findFirst().orElse(null);
        assertNotNull(bEnemy);

        final IEnemy cEnemy = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 2).findFirst().orElse(null);
        assertNotNull(cEnemy);
    }

    @Test
    public void testMoveEnemies() {
        CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        RandomMovementStrategy randomMovementStrategy = new RandomMovementStrategy(collisionDetectionService);
        enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyMovementService.addMovementStrategy(randomMovementStrategy);
        enemyService = new EnemyService(enemyMovementService);

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

        enemyService.init(new EnemyConfigParser().parse(enemyMapArray, new DefaultEnemyFactory(collisionDetectionService, new IHeroProviderMock())));
        final Collection<IEnemy> enemies = enemyService.getEnemies();

        assertNotNull(enemies);
        assertEquals(1, enemies.size());

        final IEnemy aEnemy = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

        GameContext.initialize(tileMap, new ItemServiceMock(), new HeroServiceMock(), enemyService, "map");
        final GameContext gameContext = GameContext.getInstance();

        for (int i = 0; i < 20; i++) {
            final List<MovementResult> results = enemyService.moveEnemies(gameContext);
            assertEquals(enemies.size(), results.size());

            // Since the enemies move randomly (so we do not know the target position),
            // check again that all moves were valid.
            for (MovementResult result : results) {
                enemyMovementService
                        .isValidMovePositionWithinMap(tileMap, result.getOldX(), result.getOldY(), result.getNewX(), result.getNewY());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        GameContext.resetInstance();
    }

}
