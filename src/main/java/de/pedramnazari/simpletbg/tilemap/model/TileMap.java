package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Arrays;

public class TileMap {

    private final String mapId;

    private final Tile[][] tiles;

    public TileMap(final String mapId, final Tile[][] tiles) {
        this.mapId = mapId;
        this.tiles = tiles;
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
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
