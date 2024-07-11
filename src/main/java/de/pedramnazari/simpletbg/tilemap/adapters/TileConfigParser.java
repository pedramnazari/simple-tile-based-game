package de.pedramnazari.simpletbg.tilemap.adapters;

import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.service.ITileFactory;

public class TileConfigParser {

    public Tile[][] parse(int[][] mapConfig, ITileFactory tileFactory) {
        final Tile[][] tiles = new Tile[mapConfig.length][mapConfig[0].length];

        for (int row = 0; row < mapConfig.length; row++) {
            for (int col = 0; col < mapConfig[0].length; col++) {
                int tileType = mapConfig[row][col];
                tiles[row][col] = tileFactory.createElement(tileType, col, row);
            }
        }

        return tiles;
    }

}
