package de.pedramnazari.simpletbg.model;

import java.util.Arrays;

public class TileMap {

    private final ITileFactory tileFactory;
    private final String mapId;

    private Tile[][] tiles;

    public TileMap(ITileFactory tileFactory, final String mapId, final int[][] mapConfig) {
        this.tileFactory = tileFactory;
        this.mapId = mapId;

        this.load(mapConfig);
    }

    public TileMap(final String mapId, final Tile[][] tiles) {
        tileFactory = null;
        this.mapId = mapId;
        this.tiles = tiles;
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
    }

    // TODO: Move to TileMapService?
    private void load(int[][] mapConfig) {
        tiles = new Tile[mapConfig.length][mapConfig[0].length];

        for (int row = 0; row < mapConfig.length; row++) {
            for (int col = 0; col < mapConfig[0].length; col++) {
                int tileType = mapConfig[row][col];
                tiles[row][col] = tileFactory.createElement(tileType, col, row);
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public Tile[][] getTiles() {
        return Arrays.copyOf(tiles, tiles.length);
    }

    public String getMapId() {
        return mapId;
    }
}
