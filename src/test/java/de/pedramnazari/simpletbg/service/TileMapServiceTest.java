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
        final int[][] mapArray = {
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 6},
        };

        final TileMapConfig mapConfig = new TileMapConfig("1", mapArray);

        final int width = mapArray[0].length;
        final int height = mapArray.length;


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
        TileMapConfig mapConfig = new TileMapConfig("1", new int[][]{{6, 3, 1}, {5, 4, 2}});

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
        final String idMap1 = "map1";
        final String idMap2 = "map2";
        final String idMap3 = "map3";
        final String idMap4 = "map4";

        final TileMap map1 = new TileMap(idMap1, new int[][]{{1, 1, 1}, {1, 1, 1}});
        final TileMap map2 = new TileMap(idMap2, new int[][]{{2, 2, 2}, {2, 2, 2}});
        final TileMap map3 = new TileMap(idMap3, new int[][]{{3, 3, 3}, {3, 3, 3}});
        final TileMap map4 = new TileMap(idMap4, new int[][]{{4, 4, 4}, {4, 4, 4}});

        /*      map1    map2
                map3    map4
         */
        final MapNavigator mapNavigator = new MapNavigator();

        mapNavigator.addMap(map1, idMap1);
        mapNavigator.addMap(map2, idMap2);
        mapNavigator.addMap(map3, idMap3);
        mapNavigator.addMap(map4, idMap4);

        mapNavigator.addConnection(idMap1, MoveDirections.RIGHT, idMap2);
        mapNavigator.addConnection(idMap1, MoveDirections.DOWN, idMap3);
        mapNavigator.addConnection(idMap2, MoveDirections.LEFT, idMap1);
        mapNavigator.addConnection(idMap2, MoveDirections.DOWN, idMap4);
        mapNavigator.addConnection(idMap3, MoveDirections.UP, idMap1);
        mapNavigator.addConnection(idMap3, MoveDirections.RIGHT, idMap4);
        mapNavigator.addConnection(idMap4, MoveDirections.LEFT, idMap3);
        mapNavigator.addConnection(idMap4, MoveDirections.UP, idMap2);

        final TileMap tileMap = tileMapService.createAndInitMap(mapNavigator, idMap1);
        assertNotNull(tileMap);

        assertEquals(idMap1, tileMapService.getCurrentMapIndex());

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        tileMapService.moveHero(MoveDirections.DOWN);

        assertEquals(1, hero.getX());
        assertEquals(1, hero.getY());
        assertEquals(idMap1, tileMapService.getCurrentMapIndex());

        // Move to top of map 3 (id 2)
        tileMapService.moveHero(MoveDirections.DOWN);
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());
        assertEquals(idMap3, tileMapService.getCurrentMapIndex());

        // Move to left side of map 4 (id 3)
        tileMapService.moveHero(MoveDirections.RIGHT);
        tileMapService.moveHero(MoveDirections.RIGHT);
        assertEquals(0, hero.getX());
        assertEquals(0, hero.getY());
        assertEquals(idMap4, tileMapService.getCurrentMapIndex());

        // Move to bottom of map 2 (id 1)
        tileMapService.moveHero(MoveDirections.UP);
        assertEquals(0, hero.getX());
        assertEquals(1, hero.getY());
        assertEquals(idMap2, tileMapService.getCurrentMapIndex());

        // Move to right side of map 1 (id 0)
        tileMapService.moveHero(MoveDirections.LEFT);
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());
        assertEquals(idMap1, tileMapService.getCurrentMapIndex());


    }


}
