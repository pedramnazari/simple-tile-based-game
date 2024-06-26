package de.pedramnazari.simpletbg.config;

import de.pedramnazari.simpletbg.controller.TileMapController;
import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.Inventory;
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

        final Inventory inventory = new Inventory();
        final Hero hero = new Hero(inventory, 1, 0);
        final TileMapService tileMapService = new TileMapService(
                new DefaultTileFactory(new DefaultItemFactory()),
                new DefaultItemFactory(), new MovementService(),
                hero);
        final TileMapController controller = new TileMapController(tileMapService);
        controller.startGameUsingMap(mapConfig, itemConfig);

        return controller;
    }
}
