package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Arrays;
import java.util.Collection;

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

    public void setTile(int x, int y, Tile tile) {
        tiles[y][x] = tile;
        // TODO: set x and y in tile?
    }

    public boolean isWithinBounds(int x, int y) {
        return (x >= 0) && (x < getWidth()) && (y >= 0) && (y < getHeight());
    }

    public Tile[][] getTiles() {
        return Arrays.copyOf(tiles, tiles.length);
    }

    public String getMapId() {
        return mapId;
    }

    public Collection<Tile> getTilesOfType(TileType type) {
        return Arrays.stream(tiles)
                .flatMap(Arrays::stream)
                .filter(tile -> tile.getType() == type.getType())
                .toList();
    }
}
