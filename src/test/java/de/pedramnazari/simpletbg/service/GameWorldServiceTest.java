package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.adapters.ItemConfigParser;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.tile.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tile.model.MoveDirection;
import de.pedramnazari.simpletbg.tile.model.Tile;
import de.pedramnazari.simpletbg.tile.model.TileMap;
import de.pedramnazari.simpletbg.tile.model.TileType;
import de.pedramnazari.simpletbg.tile.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tile.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tile.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameWorldServiceTest {

    private static final int O = TileType.EMPTY.getType();

    private GameWorldService gameWorldService;
    private Hero hero;
    private DefaultTileFactory tileFactory;

    @BeforeEach
    public void setUp() {
        tileFactory = new DefaultTileFactory(new DefaultItemFactory());


        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        gameWorldService = new GameWorldService(tileFactory,
                new DefaultItemFactory(),
                new HeroService(new DefaultHeroFactory(), new HeroMovementService(collisionDetectionService)),
                new EnemyService(new DefaultEnemyFactory(), new EnemyMovementService(new RandomMovementStrategy(collisionDetectionService), collisionDetectionService)));

        hero = gameWorldService.getHero();
    }

    @Test
    public void testCreateAndInitMap() {
        final int[][] mapArray = {
                {3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 6},
        };

        final int width = mapArray[0].length;
        final int height = mapArray.length;

        assertFalse(gameWorldService.isInitialized());
        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapArray, tileFactory), 1, 0);
        assertTrue(gameWorldService.isInitialized());

        assertNotNull(tileMap);
        assertEquals(width, tileMap.getWidth());
        assertEquals(height, tileMap.getHeight());

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

        final Tile cTile = tileMap.getTile(width - 1, height - 1);
        assertNotNull(cTile);
        assertEquals(6, cTile.getType());
        assertEquals(width - 1, cTile.getX());
        assertEquals(height - 1, cTile.getY());
    }

    @Test
    public void testMoveHeroWithinSingleMap() {
        final int[][] mapConfig = new int[][]{{6, 3, 1}, {5, 4, 2}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 1, 0);
        assertNotNull(tileMap);

        final Hero hero = gameWorldService.getHero();
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        assertNull(hero.getMoveDirection().orElse(null));
        gameWorldService.moveHero(MoveDirection.UP);


        // Same position as before
        assertNull(hero.getMoveDirection().orElse(null));
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHero(MoveDirection.LEFT);

        assertEquals(MoveDirection.LEFT, hero.getMoveDirection().orElse(null));
        assertEquals(0, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHero(MoveDirection.RIGHT);
        assertEquals(MoveDirection.RIGHT, hero.getMoveDirection().orElse(null));
        gameWorldService.moveHero(MoveDirection.RIGHT);
        assertEquals(MoveDirection.RIGHT, hero.getMoveDirection().orElse(null));

        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHero(MoveDirection.RIGHT);
        assertEquals(MoveDirection.RIGHT, hero.getMoveDirection().orElse(null));

        // Same position as before
        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHero(MoveDirection.DOWN);

        assertEquals(MoveDirection.DOWN, hero.getMoveDirection().orElse(null));
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        gameWorldService.moveHero(MoveDirection.DOWN);
        assertEquals(MoveDirection.DOWN, hero.getMoveDirection().orElse(null));

        // Same position as before
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        gameWorldService.moveHero(MoveDirection.UP);
        assertEquals(MoveDirection.UP, hero.getMoveDirection().orElse(null));

        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());
    }

    @Test
    public void testMoveHeroWithinSingleMapWithObstacles() {
        final int[][] mapConfig = new int[][]{
                {6, 3, 1},
                {6, 11, 1},
                {5, 4, 2}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 1, 0);
        assertNotNull(tileMap);

        final Hero hero = gameWorldService.getHero();

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        // Approach:
        // ensure that the hero does not move to
        // the obstacle tiles no matter the direction

        gameWorldService.moveHero(MoveDirection.DOWN);

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHero(MoveDirection.LEFT);
        gameWorldService.moveHero(MoveDirection.DOWN);
        gameWorldService.moveHero(MoveDirection.RIGHT);

        assertEquals(0, hero.getX());
        assertEquals(1, hero.getY());

        gameWorldService.moveHero(MoveDirection.DOWN);
        gameWorldService.moveHero(MoveDirection.RIGHT);
        gameWorldService.moveHero(MoveDirection.UP);

        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());

        gameWorldService.moveHero(MoveDirection.RIGHT);
        gameWorldService.moveHero(MoveDirection.UP);
        gameWorldService.moveHero(MoveDirection.LEFT);

        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());
    }

    @Test
    public void testMoveHeroWithinSingleMapCollectingItems() {
        final int[][] mapConfig = new int[][]{
                {6, 3, 1},
                {6, 11, 5},
                {5, 4, 2}};

        final int[][] itemsConfig = new int[][]{
                {O, O, O},
                {O, O, 100},
                {O, O, O}};

        final int[][] enemiesConfig = new int[][]{
                {O, O, O},
                {O, O, O},
                {O, O, O}};

        final TileMap tileMap = gameWorldService.createAndInitMap(
                new TileConfigParser().parse(mapConfig, tileFactory),
                new ItemConfigParser().parse(itemsConfig, new DefaultItemFactory()),
                new EnemyConfigParser().parse(enemiesConfig, new DefaultEnemyFactory()),
                1, 0);
        assertNotNull(tileMap);

        final Collection<Item> items = gameWorldService.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());

        final Item item = items.iterator().next();

        assertNotNull(item);
        assertEquals(DefaultTileFactory.ITEM_MAGIC_BLACK_KEY_NAME, item.getName());
        assertEquals(DefaultTileFactory.ITEM_MAGIC_BLACK_KEY_DESC, item.getDescription());

        final Hero hero = gameWorldService.getHero();
        assertNotNull(hero);
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        final Inventory inventory = hero.getInventory();
        assertEquals(0, inventory.getItems().size());

        gameWorldService.moveHeroToRight();
        gameWorldService.moveHeroDown();

        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        // Item collected
        // ...in inventory
        assertEquals(1, inventory.getItems().size());

        // ...removed from map/tile
        final Collection<Item> itemsAfterMove = gameWorldService.getItems();
        assertEquals(0, itemsAfterMove.size());
    }
}
