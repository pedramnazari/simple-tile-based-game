package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.model.RushCreature;
import de.pedramnazari.simpletbg.character.enemy.model.SummonerEnemy;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SummonerEnemyTest {

    private static final int O = TileType.EMPTY.getType();
    private static final int SUMMONER = TileType.ENEMY_SUMMONER.getType();
    private static final int RUSH = TileType.ENEMY_RUSH_CREATURE.getType();
    private static final int F = TileType.FLOOR1.getType();

    private IEnemyService enemyService;

    @BeforeEach
    public void setUp() {
        GameContext.resetInstance();
    }

    @Test
    public void testSummonerSpawnsRushCreatures() {
        int[][] enemyMapArray = new int[][]{
                {O, O, O, O, O},
                {O, O, SUMMONER, O, O},
                {O, O, O, O, O},
                {O, O, O, O, O},
                {O, O, O, O, O},
        };

        final int[][] tileMapArray = new int[][]{
                {F, F, F, F, F},
                {F, F, F, F, F},
                {F, F, F, F, F},
                {F, F, F, F, F},
                {F, F, F, F, F},
        };

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        final IHeroProvider heroProvider = new IHeroProviderMock();

        final DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory(collisionDetectionService, heroProvider);
        enemyService = new EnemyService(enemyMovementService);

        final TileMap tileMap = TileMapTestHelper.createMapUsingDefaults("map", tileMapArray);

        enemyService.init(new EnemyConfigParser().parse(enemyMapArray, enemyFactory));
        final Collection<IEnemy> enemies = enemyService.getEnemies();

        assertEquals(1, enemies.size());
        assertTrue(enemies.stream().anyMatch(e -> e instanceof SummonerEnemy));

        GameContext.initialize(tileMap, new ItemServiceMock(), new HeroServiceMock(), enemyService, "map");
        final GameContext gameContext = GameContext.getInstance();

        // Move enemies for several turns to trigger spawning
        for (int i = 0; i < 6; i++) {
            enemyService.moveEnemies(gameContext);
        }

        // After 5+ turns, a rush creature should have been spawned
        final Collection<IEnemy> updatedEnemies = enemyService.getEnemies();
        assertTrue(updatedEnemies.size() > 1, "Expected more than one enemy after spawning");
        
        // Check that a rush creature exists
        boolean hasRushCreature = updatedEnemies.stream()
                .anyMatch(e -> e instanceof RushCreature);
        assertTrue(hasRushCreature, "Expected a RushCreature to be spawned");
    }

    @Test
    public void testRushCreatureIsFragile() {
        int[][] enemyMapArray = new int[][]{
                {O, O, O},
                {O, RUSH, O},
                {O, O, O},
        };

        final int[][] tileMapArray = new int[][]{
                {F, F, F},
                {F, F, F},
                {F, F, F},
        };

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        final IHeroProvider heroProvider = new IHeroProviderMock();

        final DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory(collisionDetectionService, heroProvider);
        enemyService = new EnemyService(enemyMovementService);

        enemyService.init(new EnemyConfigParser().parse(enemyMapArray, enemyFactory));
        final Collection<IEnemy> enemies = enemyService.getEnemies();

        assertEquals(1, enemies.size());
        
        IEnemy rushCreature = enemies.iterator().next();
        assertTrue(rushCreature instanceof RushCreature);
        
        // Rush creatures should have low health (10)
        assertEquals(10, rushCreature.getHealth());
        
        // Any hit should kill it (decrease health by more than 10)
        rushCreature.decreaseHealth(10);
        assertEquals(0, rushCreature.getHealth());
    }

    @Test
    public void testSummonerHasNormalHealth() {
        int[][] enemyMapArray = new int[][]{
                {O, O, O},
                {O, SUMMONER, O},
                {O, O, O},
        };

        final int[][] tileMapArray = new int[][]{
                {F, F, F},
                {F, F, F},
                {F, F, F},
        };

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        final IHeroProvider heroProvider = new IHeroProviderMock();

        final DefaultEnemyFactory enemyFactory = new DefaultEnemyFactory(collisionDetectionService, heroProvider);
        enemyService = new EnemyService(enemyMovementService);

        enemyService.init(new EnemyConfigParser().parse(enemyMapArray, enemyFactory));
        final Collection<IEnemy> enemies = enemyService.getEnemies();

        assertEquals(1, enemies.size());
        
        IEnemy summoner = enemies.iterator().next();
        assertTrue(summoner instanceof SummonerEnemy);
        
        // Summoners have normal health (100)
        assertEquals(100, summoner.getHealth());
    }

    @AfterEach
    public void tearDown() {
        GameContext.resetInstance();
    }
}
