package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.ITileFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MovementServiceTest {

    private static final int F = TileType.FLOOR.getType();
    private static final int W = TileType.WALL.getType();

    private HeroMovementService heroMovementService;
    private GameWorldService gameWorldService;
    private IHero hero;
    private ITileFactory tileFactory;

    @BeforeEach
    public void setUp() {
        tileFactory = new DefaultTileFactory();

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        heroMovementService = new HeroMovementService(collisionDetectionService);

        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyMovementService.addMovementStrategy(new RandomMovementStrategy(collisionDetectionService));

        gameWorldService = new GameWorldService(
                new ItemService(),
                new HeroService(new DefaultHeroFactory(), heroMovementService, new HeroAttackService()),
                new EnemyService(enemyMovementService));
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

        final GameContext gameContext = new GameContextBuilder()
                .setTileMap(tileMap)
                .setHero(hero)
                .setItemService(gameWorldService.getItemService())
                .build();

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




}
