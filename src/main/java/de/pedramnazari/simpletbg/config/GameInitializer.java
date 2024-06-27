package de.pedramnazari.simpletbg.config;

import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.model.TileType;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.*;

public class GameInitializer {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY.getType();

    public static TileMapController initGame() {
        final TileMapConfig mapConfig = AllTileMapConfigData.getMapConfig("2");

        // TODO: move to AllTileMapConfigData
        final TileMapConfig itemConfig = new TileMapConfig("item2", new int[][]{
                {O, O, O, O, O, 100, O, O, O, O},
                {O, O, O, 100, O, O, 100, O, O, O},
                {O, O, O, O, O, O, O, O, O, 100},
                {O, 100, O, O, O, O, O, O, O, O},
                {O, O, O, 100, O, O, 100, O, O, O},

        });

        final TileMapConfig enemyConfig = new TileMapConfig("enemy2", new int[][]{
                {O, O, O, O, O, O, O, O, E, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O},
                {O, E, O, O, O, O, O, O, O, O},

        });

        final TileMapService tileMapService = new TileMapService(
                new DefaultTileFactory(new DefaultItemFactory()),
                new DefaultItemFactory(),
                new HeroService(new DefaultHeroFactory(), new HeroMovementService()),
                new EnemyService(new DefaultEnemyFactory(), new EnemyMovementService()));
        final TileMapController controller = new TileMapController(tileMapService);
        controller.startGameUsingMap(mapConfig, itemConfig, enemyConfig, 1, 0);

        return controller;
    }
}
