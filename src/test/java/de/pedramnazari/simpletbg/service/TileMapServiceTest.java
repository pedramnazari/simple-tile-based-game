package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TileMapServiceTest {

    private TileMapService tileMapService;
    private Hero hero;

    @BeforeEach
    public void setUp() {
        hero = new Hero(1, 0);
        tileMapService = new TileMapService(hero);
    }

    @Test
    public void testCreateAndInitMap() {
        final int[][] mapConfig = {
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 6},
        };

        final int width = mapConfig[0].length;
        int height = mapConfig.length;

        final TileMap tileMap = tileMapService.createAndInitMap(mapConfig);
        assertNotNull(tileMap);
        assertEquals(width, tileMap.getWidth());
        assertEquals(height, tileMap.getHeight());

        final Tile aTile = tileMap.getTile(0, 0);
        assertNotNull(aTile);
        assertEquals(1, aTile.getType());
        assertEquals(0, aTile.getX());
        assertEquals(0, aTile.getY());

        final Tile bTile = tileMap.getTile(5, 1);
        assertNotNull(bTile);
        assertEquals(2, bTile.getType());
        assertEquals(5, bTile.getX());
        assertEquals(1, bTile.getY());

        final Tile cTile = tileMap.getTile(width - 1, height - 1);
        assertNotNull(cTile);
        assertEquals(6, cTile.getType());
        assertEquals(width - 1, cTile.getX());
        assertEquals(height - 1, cTile.getY());
    }

    @Test
    public void testMoveHeroWithinSingleMap() {
        final int[][] mapConfig = {
                {6, 3, 1},
                {5, 4, 2}
        };

        final TileMap tileMap = tileMapService.createAndInitMap(mapConfig);
        assertNotNull(tileMap);

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.UP);

        // Same position as before
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.LEFT);

        assertEquals(0, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.RIGHT);
        tileMapService.moveHero(MoveDirections.RIGHT);

        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.RIGHT);

        // Same position as before
        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.DOWN);

        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        tileMapService.moveHero(MoveDirections.DOWN);

        // Same position as before
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        tileMapService.moveHero(MoveDirections.UP);

        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());
    }

    @Test
    public void testMoveHeroBetweenSeveralMaps() {
        /*      map1    map2
                map3    map4
         */
        int[][] map1 = {{1, 1, 1}, {1, 1, 1}};
        int[][] map2 = {{2, 2, 2}, {2, 2, 2}};
        int[][] map3 = {{3, 3, 3}, {3, 3, 3}};
        int[][] map4 = {{4, 4, 4}, {4, 4, 4}};


        final MapNavigator mapNavigator = new MapNavigator();

        mapNavigator.addMap(map1);
        mapNavigator.addMap(map2);
        mapNavigator.addMap(map3);
        mapNavigator.addMap(map4);

        mapNavigator.addConnection(0, MoveDirections.RIGHT, 1);
        mapNavigator.addConnection(0, MoveDirections.DOWN, 2);
        mapNavigator.addConnection(1, MoveDirections.LEFT, 0);
        mapNavigator.addConnection(1, MoveDirections.DOWN, 3);
        mapNavigator.addConnection(2, MoveDirections.UP, 0);
        mapNavigator.addConnection(2, MoveDirections.RIGHT, 3);
        mapNavigator.addConnection(3, MoveDirections.LEFT, 2);
        mapNavigator.addConnection(3, MoveDirections.UP, 1);

        final TileMap tileMap = tileMapService.createAndInitMap(mapNavigator, 0);
        assertNotNull(tileMap);

        assertEquals(0, tileMapService.getCurrentMapIndex());

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.DOWN);

        assertEquals(1, hero.getX());
        assertEquals(1, hero.getY());
        assertEquals(0, tileMapService.getCurrentMapIndex());

        // Move to top of map 3 (id 2)
        tileMapService.moveHero(MoveDirections.DOWN);
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());
        assertEquals(2, tileMapService.getCurrentMapIndex());

        // Move to left side of map 4 (id 3)
        tileMapService.moveHero(MoveDirections.RIGHT);
        tileMapService.moveHero(MoveDirections.RIGHT);
        assertEquals(0, hero.getX());
        assertEquals(0, hero.getY());
        assertEquals(3, tileMapService.getCurrentMapIndex());

        // Move to bottom of map 2 (id 1)
        tileMapService.moveHero(MoveDirections.UP);
        assertEquals(0, hero.getX());
        assertEquals(1, hero.getY());
        assertEquals(1, tileMapService.getCurrentMapIndex());

        // Move to right side of map 1 (id 0)
        tileMapService.moveHero(MoveDirections.LEFT);
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());
        assertEquals(0, tileMapService.getCurrentMapIndex());


    }


}
