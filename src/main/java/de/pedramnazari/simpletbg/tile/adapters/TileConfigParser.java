package de.pedramnazari.simpletbg.tile.adapters;

import de.pedramnazari.simpletbg.tile.model.ITileFactory;
import de.pedramnazari.simpletbg.tile.model.Tile;

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
