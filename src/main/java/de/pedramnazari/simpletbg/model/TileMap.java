package de.pedramnazari.simpletbg.model;

import java.util.Arrays;

public class TileMap {

    private final int width;
    private final int height;

    private final Tile[][] tiles;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // TODO: Move to TileMapService?
    public void load(int[][] mapConfig) {
        if ( (mapConfig.length != height) || (mapConfig[0].length != width)) {
            throw new IllegalArgumentException("No valid map configuration");
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int tileType = mapConfig[row][col];
                tiles[row][col] = new Tile(tileType, col, row);
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public Tile[][] getTiles() {
        return Arrays.copyOf(tiles, tiles.length);
    }
}
