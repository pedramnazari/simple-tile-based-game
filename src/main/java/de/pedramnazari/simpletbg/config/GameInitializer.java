package de.pedramnazari.simpletbg.config;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.inventory.adapters.ItemConfigParser;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.service.CollisionDetectionService;
import de.pedramnazari.simpletbg.tile.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tile.model.Tile;
import de.pedramnazari.simpletbg.tile.model.TileType;
import de.pedramnazari.simpletbg.tile.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tile.service.TileMapService;
import de.pedramnazari.simpletbg.tile.service.navigation.LeftToRightMovementStrategy;

import java.util.Collection;

public class GameInitializer {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY.getType();

    public static TileMapController initAndStartGame() {

        // TODO: move to AllTileMapConfigData
        final int[][] mapConfig = new int[][]{
                {1, 1, 11, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 11, 1, 1, 1, 11, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 11, 1, 1, 1, 1, 11, 1, 1},

        };

        final int[][] itemConfig = new int[][]{
                {O, O, O, O, O, 100, O, O, O, O},
                {O, O, O, 100, O, O, 100, O, O, O},
                {O, O, O, O, O, O, O, O, O, 100},
                {O, 100, O, O, O, O, O, O, O, O},
                {O, O, O, 100, O, O, 100, O, O, O},

        };

        final int[][] enemyConfig = new int[][]{
                {O, O, O, O, O, O, O, O, E, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, E, O, O, O, O, O, O, O, O},

        };

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyService enemyService =
                new EnemyService(new DefaultEnemyFactory(), new EnemyMovementService(new LeftToRightMovementStrategy(collisionDetectionService), collisionDetectionService));

        DefaultItemFactory itemFactory = new DefaultItemFactory();
        DefaultTileFactory tileFactory = new DefaultTileFactory(itemFactory);
        final TileMapService tileMapService = new TileMapService(
                tileFactory,
                itemFactory,
                new HeroService(new DefaultHeroFactory(), new HeroMovementService(collisionDetectionService)),
                enemyService);
        final TileMapController controller = new TileMapController(tileMapService);
        enemyService.registerObserver(controller);

        final Tile[][] tiles = new TileConfigParser().parse(mapConfig, tileFactory);
        final Collection<Item> items = new ItemConfigParser().parse(itemConfig, itemFactory);
        final Collection<Enemy> enemies = new EnemyConfigParser().parse(enemyConfig, new DefaultEnemyFactory());

        controller.startGameUsingMap(tiles, items, enemies, 1, 0);

        return controller;
    }
}
