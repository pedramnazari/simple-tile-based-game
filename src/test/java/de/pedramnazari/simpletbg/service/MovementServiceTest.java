package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.tile.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tile.model.ITileFactory;
import de.pedramnazari.simpletbg.tile.model.Point;
import de.pedramnazari.simpletbg.tile.model.TileMap;
import de.pedramnazari.simpletbg.tile.model.TileType;
import de.pedramnazari.simpletbg.tile.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tile.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tile.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tile.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MovementServiceTest {

    private static final int F = TileType.FLOOR.getType();
    private static final int W = TileType.WALL.getType();

    private HeroMovementService movementService;
    private GameWorldService gameWorldService;
    private Hero hero;
    private ITileFactory tileFactory;

    @BeforeEach
    public void setUp() {
        tileFactory = new DefaultTileFactory(new DefaultItemFactory());

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        movementService = new HeroMovementService(collisionDetectionService);

        gameWorldService = new GameWorldService(tileFactory,
                new DefaultItemFactory(),
                new HeroService(new DefaultHeroFactory(), movementService),
                new EnemyService(new DefaultEnemyFactory(), new EnemyMovementService(new RandomMovementStrategy(collisionDetectionService), collisionDetectionService)));
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

        final Set<Point> validPositions = movementService.calcValidMovePositionsWithinMap(tileMap, hero);
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

        final Set<Point> validPositions = movementService.calcValidMovePositionsWithinMap(tileMap, hero);
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
                .setItemService(gameWorldService)
                .build();

        MovementResult result = movementService.moveElementToPositionWithinMap(gameContext, hero, 1, 2);
        assertTrue(result.hasElementMoved());
        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());

        result = movementService.moveElementToPositionWithinMap(gameContext, hero, 2, 2);
        assertFalse(result.hasElementMoved());
        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());

        // Jumps are not allowed
        result = movementService.moveElementToPositionWithinMap(gameContext, hero, 1, 0);
        assertFalse(result.hasElementMoved());

        result = movementService.moveElementToPositionWithinMap(gameContext, hero, 0, 1);
        assertFalse(result.hasElementMoved());

        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());
    }




}
