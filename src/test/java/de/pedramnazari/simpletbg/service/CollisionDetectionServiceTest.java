package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectionServiceTest {

    private static final int O = TileType.EMPTY.getType();
    private static final int F = TileType.FLOOR.getType();
    private static final int E = TileType.ENEMY.getType();
    private static final int W = TileType.WALL.getType();

    private CollisionDetectionService collisionDetectionService;

    @BeforeEach
    public void setUp() {
        collisionDetectionService = new CollisionDetectionService();
    }

    @Test
    public void testIsCollisionWithObstacle() {
        final TileMapConfig mapConfig = new TileMapConfig("1", new int[][]{
                {F, F, W},
                {F, W, W},
                {W, F, F}});

        final TileMap tileMap = new TileMap(new DefaultTileFactory(new DefaultItemFactory()), mapConfig.getMapId(), mapConfig.getMap());
        assertNotNull(tileMap);

        assertFalse(collisionDetectionService.isCollisionWithObstacle(tileMap, 0, 0));
        assertFalse(collisionDetectionService.isCollisionWithObstacle(tileMap, 1, 0));
        assertTrue(collisionDetectionService.isCollisionWithObstacle(tileMap, 2, 0));
        assertFalse(collisionDetectionService.isCollisionWithObstacle(tileMap, 0, 1));
        assertTrue(collisionDetectionService.isCollisionWithObstacle(tileMap, 1, 1));
        assertTrue(collisionDetectionService.isCollisionWithObstacle(tileMap, 2, 1));
        assertTrue(collisionDetectionService.isCollisionWithObstacle(tileMap, 0, 2));
        assertFalse(collisionDetectionService.isCollisionWithObstacle(tileMap, 1, 2));
        assertFalse(collisionDetectionService.isCollisionWithObstacle(tileMap, 2, 2));
    }

    @Test
    public void testCheckCollision_BetweenHeroAndEnemies() {
        final TileMapConfig enemiesConfig = new TileMapConfig("1", new int[][]{
                {O, E, O},
                {O, O, O},
                {O, O, E}});


        final Hero hero = new Hero(0, 0);

        final Collection<Enemy> enemies = new DefaultEnemyFactory().createElementsUsingTileMapConfig(enemiesConfig);
        assertEquals(2, enemies.size());

        final Enemy enemy1 = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(enemy1);

        final Enemy enemy2 = enemies.stream().filter(e -> e.getX() == 2 && e.getY() == 2).findFirst().orElse(null);
        assertNotNull(enemy2);

        assertTrue(collisionDetectionService.isCollision(hero, hero));
        assertTrue(collisionDetectionService.isCollision(enemy1, enemy1));
        assertTrue(collisionDetectionService.isCollision(enemy2, enemy2));


        assertFalse(collisionDetectionService.isCollision(hero, enemy1));
        assertFalse(collisionDetectionService.isCollision(hero, enemy2));
        assertFalse(collisionDetectionService.isCollision(enemy1, hero));
        assertFalse(collisionDetectionService.isCollision(enemy2, hero));

        Collection<? extends IMoveableTileElement> collisionElements = collisionDetectionService.getCollidingElements(hero, enemies);
        assertEquals(0, collisionElements.size());

        hero.setX(1);
        hero.setY(0);

        assertTrue(collisionDetectionService.isCollision(hero, enemy1));
        assertTrue(collisionDetectionService.isCollision(enemy1, hero));

        assertFalse(collisionDetectionService.isCollision(hero, enemy2));

        collisionElements = collisionDetectionService.getCollidingElements(hero, enemies);
        assertEquals(1, collisionElements.size());
    }
}
