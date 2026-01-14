package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.adapters.ItemConfigParser;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.TileMapService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.RandomMovementStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameWorldServiceTest {

    private static final int O = TileType.EMPTY.getType();

    private GameWorldService gameWorldService;
    private IHero hero;
    private DefaultTileFactory tileFactory;

    @BeforeEach
    public void setUp() {
        GameContext.resetInstance();

        tileFactory = new DefaultTileFactory();


        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyMovementService.addMovementStrategy(new RandomMovementStrategy(collisionDetectionService));


        final ItemService itemService = new ItemService();
        final HeroService heroService = new HeroService(
                new DefaultHeroFactory(),
                new HeroMovementService(collisionDetectionService),
                new HeroAttackService());
        heroService.addItemEventListener(itemService);

        gameWorldService = new GameWorldService(
                new TileMapService(tileFactory),
                itemService,
                heroService,
                new EnemyService(enemyMovementService),
                new CompanionServiceMock());

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

        GameContext.initialize(tileMap, gameWorldService.getItemService(), gameWorldService.getHeroService(), gameWorldService.getEnemyService(), new CompanionServiceMock(), "map");

        final IHero hero = gameWorldService.getHero();
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        assertNull(hero.getMoveDirection().orElse(null));
        gameWorldService.moveHeroUp();


        // Same position as before
        assertNull(hero.getMoveDirection().orElse(null));
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHeroToLeft();

        assertEquals(MoveDirection.LEFT, hero.getMoveDirection().orElse(null));
        assertEquals(0, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHeroToRight();
        assertEquals(MoveDirection.RIGHT, hero.getMoveDirection().orElse(null));
        gameWorldService.moveHeroToRight();
        assertEquals(MoveDirection.RIGHT, hero.getMoveDirection().orElse(null));

        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHeroToRight();
        assertEquals(MoveDirection.RIGHT, hero.getMoveDirection().orElse(null));

        // Same position as before
        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHeroDown();

        assertEquals(MoveDirection.DOWN, hero.getMoveDirection().orElse(null));
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        gameWorldService.moveHeroDown();
        assertEquals(MoveDirection.DOWN, hero.getMoveDirection().orElse(null));

        // Same position as before
        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        gameWorldService.moveHeroUp();
        assertEquals(MoveDirection.UP, hero.getMoveDirection().orElse(null));

        assertEquals(2, hero.getX());
        assertEquals(0, hero.getY());
    }

    @Test
    public void testMoveHeroWithinSingleMapWithObstacles() {
        final int[][] mapConfig = new int[][]{
                {6, 3, 1},
                {6, TileType.WALL.getType(), 1},
                {5, 4, 2}};

        final TileMap tileMap = gameWorldService.createAndInitMap(new TileConfigParser().parse(mapConfig, tileFactory), 1, 0);
        assertNotNull(tileMap);

        GameContext.initialize(tileMap, gameWorldService.getItemService(), gameWorldService.getHeroService(), gameWorldService.getEnemyService(), new CompanionServiceMock(), "map");

        final IHero hero = gameWorldService.getHero();

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        // Approach:
        // ensure that the hero does not move to
        // the obstacle tiles no matter the direction

        gameWorldService.moveHeroDown();

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        gameWorldService.moveHeroToLeft();
        gameWorldService.moveHeroDown();
        gameWorldService.moveHeroToRight();

        assertEquals(0, hero.getX());
        assertEquals(1, hero.getY());

        gameWorldService.moveHeroDown();
        gameWorldService.moveHeroToRight();
        gameWorldService.moveHeroUp();

        assertEquals(1, hero.getX());
        assertEquals(2, hero.getY());

        gameWorldService.moveHeroToRight();
        gameWorldService.moveHeroUp();
        gameWorldService.moveHeroToLeft();

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
                {O, O, TileType.ITEM_YELLOW_KEY.getType()},
                {O, O, TileType.WEAPON_SWORD.getType()}};

        final int[][] enemiesConfig = new int[][]{
                {O, O, O},
                {O, O, O},
                {O, O, O}};

        final TileMap tileMap = gameWorldService.createAndInitMap(
                new TileConfigParser().parse(mapConfig, tileFactory),
                new ItemConfigParser().parse(itemsConfig, new DefaultItemFactory()),
                new EnemyConfigParser().parse(enemiesConfig, new DefaultEnemyFactory(new CollisionDetectionService(), new IHeroProviderMock())),
                1, 0);
        assertNotNull(tileMap);

        GameContext.initialize(tileMap, gameWorldService.getItemService(), gameWorldService.getHeroService(), gameWorldService.getEnemyService(), new CompanionServiceMock(), "map");

        Collection<IItem> items = gameWorldService.getItemService().getItems();
        assertNotNull(items);
        assertEquals(2, items.size());

        IItem item = items.iterator().next();

        assertNotNull(item);
        assertEquals(DefaultItemFactory.ITEM_MAGIC_YELLOW_KEY_NAME, item.getName());
        assertEquals(DefaultItemFactory.ITEM_MAGIC_YELLOW_KEY_DESC, item.getDescription());

        final IHero hero = gameWorldService.getHero();
        assertNotNull(hero);
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        final IInventory inventory = hero.getInventory();
        assertEquals(0, inventory.getItems().size());

        gameWorldService.moveHeroToRight();
        gameWorldService.moveHeroDown();

        assertEquals(2, hero.getX());
        assertEquals(1, hero.getY());

        // Item collected
        // ...in inventory
        assertEquals(1, inventory.getItems().size());

        // ...removed from map/tile
        items = gameWorldService.getItemService().getItems();
        assertEquals(1, items.size());

        gameWorldService.moveHeroDown();
        assertEquals(2, hero.getX());
        assertEquals(2, hero.getY());

        // Weapon collected
        // ...but was placed in the hero's hand, not in the inventory.
        assertTrue(hero.getWeapon().isPresent());
        assertEquals(1, inventory.getItems().size());

        // ...removed from map/tile
        items = gameWorldService.getItemService().getItems();
        assertEquals(0, items.size());
    }

    @AfterEach
    public void tearDown() {
        GameContext.resetInstance();
    }

}
