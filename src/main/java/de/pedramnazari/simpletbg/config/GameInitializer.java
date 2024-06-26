package de.pedramnazari.simpletbg.config;

import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.repository.AllTileMapConfigData;
import de.pedramnazari.simpletbg.service.*;

public class GameInitializer {


    public static TileMapController initGame() {
        final TileMapConfig mapConfig = AllTileMapConfigData.getMapConfig("2");

        // TODO: move to AllTileMapConfigData
        final TileMapConfig itemConfig = new TileMapConfig("item2", new int[][]{
                {0, 0, 0, 0, 0, 100, 0, 0, 0, 0},
                {0, 0, 0, 100, 0, 0, 100, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 100},
                {0, 100, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 100, 0, 0, 100, 0, 0, 0},
        });

        final TileMapService tileMapService = new TileMapService(
                new DefaultTileFactory(new DefaultItemFactory()),
                new DefaultItemFactory(), new HeroService(new DefaultHeroFactory(), new HeroMovementService()));
        final TileMapController controller = new TileMapController(tileMapService);
        controller.startGameUsingMap(mapConfig, itemConfig, 1, 0);

        return controller;
    }
}
