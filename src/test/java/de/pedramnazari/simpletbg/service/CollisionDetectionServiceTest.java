package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.model.TileMapTestHelper;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectionServiceTest {

    private static final int O = TileType.EMPTY.getType();
    private static final int F = TileType.FLOOR.getType();
    private static final int E = TileType.ENEMY_LR.getType();
    private static final int W = TileType.WALL.getType();

    private CollisionDetectionService collisionDetectionService;

    @BeforeEach
    public void setUp() {
        collisionDetectionService = new CollisionDetectionService();
    }

    @Test
    public void testIsCollisionWithObstacle() {
        final int[][] mapConfig = new int[][]{
                {F, F, W},
                {F, W, W},
                {W, F, F}};

        final TileMap tileMap = TileMapTestHelper.createMapUsingDefaults(mapConfig);
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
        final int[][] enemiesConfig = new int[][]{
                                {O, E, O},
                                {O, O, O},
                                {O, O, E}};


        final IHero hero = new Hero(0, 0);

        final Collection<Enemy> enemies = new DefaultEnemyFactory(collisionDetectionService, new IHeroProviderMock()).createElementsUsingTileMapConfig(enemiesConfig);
        assertEquals(2, enemies.size());

        final Enemy enemy1 = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(enemy1);

        final Enemy enemy2 = enemies.stream().filter(e -> e.getX() == 2 && e.getY() == 2).findFirst().orElse(null);
        assertNotNull(enemy2);

        // Elements cannot have collision with themselves
        assertFalse(collisionDetectionService.isCollision(hero, hero));
        assertFalse(collisionDetectionService.isCollision(enemy1, enemy1));
        assertFalse(collisionDetectionService.isCollision(enemy2, enemy2));


        assertFalse(collisionDetectionService.isCollision(hero, enemy1));
        assertFalse(collisionDetectionService.isCollision(hero, enemy2));
        assertFalse(collisionDetectionService.isCollision(enemy1, hero));
        assertFalse(collisionDetectionService.isCollision(enemy2, hero));

        Collection<? extends IMovableTileElement> collisionElements = collisionDetectionService.getCollidingElements(hero, enemies);
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
