package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.model.bomb.Bomb;
import de.pedramnazari.simpletbg.inventory.service.ArmorService;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.ITileFactory;
import de.pedramnazari.simpletbg.tilemap.service.TileMapService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MovementServiceTest {

    private static final int F = TileType.FLOOR1.getType();
    private static final int W = TileType.WALL.getType();
    private static final int P = TileType.PORTAL.getType();
    private static final int PW = TileType.WALL_HIDING_PORTAL.getType();

    private HeroMovementService heroMovementService;
    private GameWorldService gameWorldService;
    private IHero hero;
    private ITileFactory tileFactory;
    private HeroService heroService;
    private TileMapService tileMapService;

    @BeforeEach
    public void setUp() {
        GameContext.resetInstance();

        tileFactory = new DefaultTileFactory();

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        heroMovementService = new HeroMovementService(collisionDetectionService);

        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyMovementService.addMovementStrategy(new RandomMovementStrategy(collisionDetectionService));
        
        final EnemyService enemyService = new EnemyService(enemyMovementService);

        heroService = new HeroService(new DefaultHeroFactory(), heroMovementService, new HeroAttackService());
        tileMapService = new TileMapService(tileFactory);
        gameWorldService = new GameWorldService(
                tileMapService,
                new ItemService(),
                heroService,
                enemyService,
                new CompanionServiceMock(),
                new ArmorService(enemyService));

        heroService.addHeroMovedListener(tileMapService);
        tileMapService.addCharacterMovedToSpecialTileListener(heroService);
    }

    @Test
    public void testCalcValidMovePositionsWithinMap_AtMapBoundary() {
        final int[][] mapConfig = new int[][]{
                {W, F, F},
                {F, W, W},
                {W, F, W}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 1, 0);
        assertNotNull(tileMap);

        hero = gameWorldService.getHero();
        assertNotNull(hero);

        final Set<Point> validPositions = heroMovementService.calcValidMovePositionsWithinMap(tileMap, hero);
        assertEquals(1, validPositions.size());

        final Point point = validPositions.iterator().next();
        assertEquals(2, point.getX());
        assertEquals(0, point.getY());
    }


    @Test
    public void testCalcValidMovePositionsWithinMap_AtMapEdge() {
        final int[][] mapConfig = new int[][]{
                {W, F, F},
                {F, W, W},
                {F, F, W}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 0, 2);
        assertNotNull(tileMap);

        hero = gameWorldService.getHero();
        assertNotNull(hero);

        final Set<Point> validPositions = heroMovementService.calcValidMovePositionsWithinMap(tileMap, hero);
        assertEquals(2, validPositions.size());

        final Point point1 = validPositions.stream().filter(p -> p.getX() == 0 && p.getY() == 1).findFirst().orElse(null);
        assertNotNull(point1);

        final Point point2 = validPositions.stream().filter(p -> p.getX() == 1 && p.getY() == 2).findFirst().orElse(null);
        assertNotNull(point2);
    }

    @Test
    public void testMoveElementToPositionWithinMap() {
        final int[][] mapConfig = new int[][]{
                {W, F, F},
                {F, W, W},
                {F, F, W}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 0, 2);
        assertNotNull(tileMap);

        hero = gameWorldService.getHero();
        assertNotNull(hero);

        GameContext.initialize(tileMap, gameWorldService.getItemService(), new HeroServiceMock(hero), new EnemyServiceMock(), new CompanionServiceMock(), "map");
        final GameContext gameContext = GameContext.getInstance();

        MovementResult result = heroMovementService.moveElementToPositionWithinMap(gameContext, hero, 1, 2);
        assertTrue(result.hasElementMoved());
        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());

        result = heroMovementService.moveElementToPositionWithinMap(gameContext, hero, 2, 2);
        assertFalse(result.hasElementMoved());
        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());

        // Jumps are not allowed
        result = heroMovementService.moveElementToPositionWithinMap(gameContext, hero, 1, 0);
        assertFalse(result.hasElementMoved());

        result = heroMovementService.moveElementToPositionWithinMap(gameContext, hero, 0, 1);
        assertFalse(result.hasElementMoved());

        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());
    }

    @Test
    public void testTeleport_BetweenPortals() {
        final int[][] mapConfig = new int[][]{
                {P, F, F},
                {F, W, P},
                {F, F, W}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 1, 0);
        assertNotNull(tileMap);

        hero = gameWorldService.getHero();
        assertNotNull(hero);

        GameContext.initialize(tileMap, gameWorldService.getItemService(), heroService, new EnemyServiceMock(), new CompanionServiceMock(), "map");

        MovementResult result = heroService.moveHero(MoveDirection.LEFT, GameContext.getInstance());
        assertTrue(result.hasElementMoved());
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        // Hero steps on a portal
        result = heroService.moveHero(MoveDirection.RIGHT, GameContext.getInstance());
        assertFalse(result.hasElementMoved());
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        result = heroService.moveHero(MoveDirection.DOWN, GameContext.getInstance());
        assertFalse(result.hasElementMoved());

        result = heroService.moveHero(MoveDirection.UP, GameContext.getInstance());
        assertTrue(result.hasElementMoved());
        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        // Hero steps on a portal
        result = heroService.moveHero(MoveDirection.DOWN, GameContext.getInstance());
        assertTrue(result.hasElementMoved());
        assertEquals(0, hero.getX());
        assertEquals(0, hero.getY());

        result = heroService.moveHero(MoveDirection.DOWN, GameContext.getInstance());
        assertTrue(result.hasElementMoved());
        assertEquals(0, hero.getX());
        assertEquals(1, hero.getY());
    }

    @Test
    public void testTeleport_betweenPortalsBehindWalls() {
        final int[][] mapConfig = new int[][]{
                {PW, F, F},
                {F, W, P},
                {F, F, W}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 1, 0);
        assertNotNull(tileMap);

        hero = gameWorldService.getHero();
        assertNotNull(hero);

        GameContext.initialize(tileMap, gameWorldService.getItemService(), heroService, new EnemyServiceMock(), new CompanionServiceMock(), "map");

        final Tile wallHidingPortal = tileMap.getTile(0, 0);
        assertFalse(wallHidingPortal.isDestroyed());
        assertFalse(wallHidingPortal.isPortal());
        assertTrue(wallHidingPortal.isDestructible());
        final int hitPoints = wallHidingPortal.getHitPoints();
        assertEquals(2, hitPoints);

        // Hero cannot move to the left because of the wall
        MovementResult result = heroService.moveHero(MoveDirection.LEFT, GameContext.getInstance());
        assertFalse(result.hasElementMoved());
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());


        // Destroy the wall with a bomb...
        for (int i = 1; i <= hitPoints; i++) {
            IBomb bomb = new Bomb(1, 0, 0);
            List<Point> attackingPoints = List.of(new Point(0, 0));
            tileMapService.onBombExploded(bomb, attackingPoints);
        }

        assertEquals(0, wallHidingPortal.getHitPoints());
        assertTrue(wallHidingPortal.isDestroyed());

        // ...to reveal the portal
        final Tile portal = tileMap.getTile(0, 0);
        assertTrue(portal.isPortal());
        assertFalse(portal.isDestroyed());
        assertFalse(portal.isDestructible());


        // Hero can now move to the left to the portal...
        result = heroService.moveHero(MoveDirection.LEFT, GameContext.getInstance());
        assertTrue(result.hasElementMoved());
        // ...and teleport to the other portal
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());
    }

    @AfterEach
    public void tearDown() {
        GameContext.resetInstance();
        hero = null;
        heroMovementService = null;
        gameWorldService = null;
        tileFactory = null;
    }




}
