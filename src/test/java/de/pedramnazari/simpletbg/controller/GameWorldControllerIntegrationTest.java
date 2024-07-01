package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.RandomMovementStrategy;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameWorldControllerIntegrationTest {

    private GameWorldController controller;
    private DefaultTileFactory tileFactory;

    @BeforeEach
    public void setUp() {
        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        tileFactory = new DefaultTileFactory(new DefaultItemFactory());
        GameWorldService gameWorldService = new GameWorldService(
                tileFactory,
                new DefaultItemFactory(),
                new HeroService(new DefaultHeroFactory(), new HeroMovementService(collisionDetectionService)),
                new EnemyService(new DefaultEnemyFactory(), new EnemyMovementService(new RandomMovementStrategy(collisionDetectionService), collisionDetectionService)));

        controller = new GameWorldController(gameWorldService);
    }

    @Test
    public void testStartNewGame() {
        final int[][] map1Config = new int[][] {
                {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
                {1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
        };

        final TileMap tileMap = controller.startGameUsingMap(new TileConfigParser().parse(map1Config, tileFactory), 1, 0);
        assertNotNull(tileMap);

        assertEquals(map1Config[0].length, tileMap.getWidth());
        assertEquals(map1Config.length, tileMap.getHeight());

        final Tile aTile = tileMap.getTile(0, 0);
        assertNotNull(aTile);
        assertEquals(3, aTile.getType());
        assertEquals(0, aTile.getX());
        assertEquals(0, aTile.getY());

        final Tile bTile = tileMap.getTile(5, 1);
        assertNotNull(bTile);
        assertEquals(2, bTile.getType());
        assertEquals(5, bTile.getX());
        assertEquals(1, bTile.getY());

        final Tile cTile = tileMap.getTile(tileMap.getWidth() - 1, tileMap.getHeight() - 1);
        assertNotNull(cTile);
        assertEquals(3, cTile.getType());
        assertEquals(tileMap.getWidth() - 1, cTile.getX());
        assertEquals(tileMap.getHeight() - 1, cTile.getY());
    }

    @Test
    public void testStartGameUsingMap() {
        final int[][] mapArray = {
                {6, 3, 1},
                {5, 4, 2}
        };

        final TileMap tileMap = controller.startGameUsingMap(new TileConfigParser().parse(mapArray, tileFactory), 1, 0);
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
