package de.pedramnazari.simpletbg.config;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.adapters.ItemConfigParser;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.LeftToRightMovementStrategy;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;

import java.util.Collection;

public class GameInitializer {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY_LR.getType();
    private static final int E2 = TileType.ENEMY_TD.getType();
    private static final int E3 = TileType.ENEMY_2D.getType();

    public static GameWorldController initAndStartGame() {

        // TODO: move to AllTileMapConfigData
        final int[][] mapConfig = new int[][]{
                {1, 1, 11, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 11, 11, 11, 11},
                {11, 2, 11, 1, 1, 1, 11, 2, 2, 2},
                {11, 2, 11, 11, 11, 11, 11, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2},

        };

        final int[][] itemConfig = new int[][]{
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, 100, O, O, O, O, O, O},
                {O, O, O, O, O, 100, O, O, O, 0},
                {O, 101, O, O, O, O, O, O, O, O},
                {O, O, O, 101, O, O, 101, O, O, O},

        };

        final int[][] enemyConfig = new int[][]{
                {E3, O, O, E2, O, O, O, O, E, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, E, O, O, O, O, O, O, O, E2},

        };

        // TODO: Improve
        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyMovementService.addMovementStrategy(new LeftToRightMovementStrategy(collisionDetectionService));

        final EnemyService enemyService =
                new EnemyService(new DefaultEnemyFactory(collisionDetectionService), enemyMovementService);

        DefaultItemFactory itemFactory = new DefaultItemFactory();
        DefaultTileFactory tileFactory = new DefaultTileFactory(itemFactory);
        final GameWorldService gameWorldService = new GameWorldService(
                tileFactory,
                itemFactory,
                new HeroService(new DefaultHeroFactory(), new HeroMovementService(collisionDetectionService)),
                enemyService);
        final GameWorldController controller = new GameWorldController(gameWorldService);
        enemyService.registerObserver(controller);


        final Tile[][] tiles = new TileConfigParser().parse(mapConfig, tileFactory);
        final Collection<Item> items = new ItemConfigParser().parse(itemConfig, itemFactory);
        final Collection<Enemy> enemies = new EnemyConfigParser().parse(enemyConfig, new DefaultEnemyFactory(collisionDetectionService));

        controller.startGameUsingMap(tiles, items, enemies, 1, 0);

        return controller;
    }
}
