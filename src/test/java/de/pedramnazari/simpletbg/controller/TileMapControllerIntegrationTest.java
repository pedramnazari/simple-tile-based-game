package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.Tile;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.service.TileMapConfig;
import de.pedramnazari.simpletbg.service.TileMapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TileMapControllerIntegrationTest {

    private TileMapController controller;
    private TileMapService tileMapService;

    @BeforeEach
    public void setUp() {
        tileMapService = new TileMapService();
        controller = new TileMapController(tileMapService);
    }

    @Test
    public void testStartNewGame() {
        final TileMap tileMap = controller.startNewGame();
        assertNotNull(tileMap);

        final int[][] map1Config = TileMapConfig.getMapConfig("1");

        assertEquals(map1Config[0].length, tileMap.getWidth());
        assertEquals(map1Config.length, tileMap.getHeight());

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

        final Tile cTile = tileMap.getTile(tileMap.getWidth()-1, tileMap.getHeight()-1);
        assertNotNull(cTile);
        assertEquals(6, cTile.getType());
        assertEquals(tileMap.getWidth()-1, cTile.getX());
        assertEquals(tileMap.getHeight()-1, cTile.getY());
    }

    @Test
    public void testStartGameUsingMap() {
        final int[][] mapConfig = {
                {6, 3, 1},
                {5, 4, 2}
        };


        final TileMap tileMap = controller.startGameUsingMap(mapConfig);
        assertNotNull(tileMap);
        assertEquals(3, tileMap.getWidth());
        assertEquals(2, tileMap.getHeight());

        assertEquals(6, tileMap.getTile(0, 0).getType());
        assertEquals(3, tileMap.getTile(1, 0).getType());
        assertEquals(1, tileMap.getTile(2, 0).getType());
        assertEquals(5, tileMap.getTile(0, 1).getType());
        assertEquals(4, tileMap.getTile(1, 1).getType());
        assertEquals(2, tileMap.getTile(2, 1).getType());
    }


}
