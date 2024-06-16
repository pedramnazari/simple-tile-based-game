package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Tile;
import de.pedramnazari.simpletbg.model.TileMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TileMapServiceTest {

    private TileMapService tileMapService;

    @BeforeEach
    public void setUp() {
        tileMapService = new TileMapService();
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

        final Tile cTile = tileMap.getTile(width-1, height-1);
        assertNotNull(cTile);
        assertEquals(6, cTile.getType());
        assertEquals(width-1, cTile.getX());
        assertEquals(height-1, cTile.getY());
    }


}
