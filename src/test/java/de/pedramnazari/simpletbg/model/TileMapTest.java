package de.pedramnazari.simpletbg.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TileMapTest {


    @Test
    public void testTileMap() {

        final int[][] mapConfig = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

        final TileMap tileMap = TileMapTestHelper.createMapUsingDefaults("idMap1", mapConfig);

        assertEquals("idMap1", tileMap.getMapId());

        assertEquals(mapConfig[0].length, tileMap.getWidth());
        assertEquals(mapConfig.length, tileMap.getHeight());

        for (Tile[] tileRow : tileMap.getTiles()) {
            for (Tile t : tileRow) {
                assertNotNull(t);
            }
        }

        Tile tile = tileMap.getTile(0, 0);

        assertNotNull(tile);
        assertEquals(0, tile.getX());
        assertEquals(0, tile.getY());
        assertEquals(1, tile.getType());

        tile = tileMap.getTile(5, 1);
        assertEquals(5, tile.getX());
        assertEquals(1, tile.getY());
        assertEquals(2, tile.getType());




    }


}
