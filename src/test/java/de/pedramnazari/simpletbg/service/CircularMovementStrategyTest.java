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
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CircularMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CircularMovementStrategyTest {

    private static final int F = TileType.FLOOR.getType();
    private static final int W = TileType.WALL.getType();

    private HeroMovementService heroMovementService;
    private GameWorldService gameWorldService;
    private Hero hero;
    private ITileFactory tileFactory;
    private CircularMovementStrategy movementStrategy;

    @BeforeEach
    public void setUp() {
        tileFactory = new DefaultTileFactory(new DefaultItemFactory());

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        heroMovementService = new HeroMovementService(collisionDetectionService);

        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        movementStrategy = new CircularMovementStrategy(collisionDetectionService);
        enemyMovementService.addMovementStrategy(movementStrategy);

        gameWorldService = new GameWorldService(tileFactory,
                new DefaultItemFactory(),
                new ItemService(),
                new HeroService(new DefaultHeroFactory(), heroMovementService),
                new EnemyService(new DefaultEnemyFactory(collisionDetectionService), enemyMovementService));
    }

    @Test
    public void test() {
        final int[][] mapConfig = new int[][]{
                {F, F, F},
                {W, W, F},
                {F, F, F}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 0, 0);
        assertNotNull(tileMap);

        final Hero hero = gameWorldService.getHero();

        Point nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(1, nextMove.getX());
        assertEquals(0, nextMove.getY());

        hero.setMoveDirection(MoveDirection.RIGHT);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(2, nextMove.getX());
        assertEquals(0, nextMove.getY());

        hero.setMoveDirection(MoveDirection.RIGHT);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(2, nextMove.getX());
        assertEquals(1, nextMove.getY());

        hero.setMoveDirection(MoveDirection.DOWN);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(2, nextMove.getX());
        assertEquals(2, nextMove.getY());

        hero.setMoveDirection(MoveDirection.DOWN);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(1, nextMove.getX());
        assertEquals(2, nextMove.getY());

        hero.setMoveDirection(MoveDirection.LEFT);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(0, nextMove.getX());
        assertEquals(2, nextMove.getY());

        hero.setMoveDirection(MoveDirection.LEFT);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(1, nextMove.getX());
        assertEquals(2, nextMove.getY());

        hero.setMoveDirection(MoveDirection.RIGHT);
        hero.setX(nextMove.getX());
        hero.setY(nextMove.getY());

        nextMove = movementStrategy.calcNextMove(tileMap, hero);
        assertEquals(2, nextMove.getX());
        assertEquals(2, nextMove.getY());

    }
}
