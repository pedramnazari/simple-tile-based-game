package de.pedramnazari.simpletbg.model;

import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;

public class TileMapTestHelper {

    public static TileMap createMapUsingDefaults(String mapId, int[][] mapConfig) {
        return new TileMap(mapId,
                new TileConfigParser().parse(mapConfig, new DefaultTileFactory(new DefaultItemFactory())));
    }

    public static TileMap createMapUsingDefaults(int[][] mapConfig) {
        return new TileMap("",
                new TileConfigParser().parse(mapConfig, new DefaultTileFactory(new DefaultItemFactory())));
    }


}
