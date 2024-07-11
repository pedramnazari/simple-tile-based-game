package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.Tile;

// TODO: separate TileFactory for obstacles, items etc
public class DefaultTileFactory extends AbstractTileMapElementFactory<Tile> implements ITileFactory {

    public DefaultTileFactory() {
    }


    @Override
    protected Tile createNonEmptyElement(int type, int x, int y) {
        Tile tile;
        if (type < 100) {
            tile = createFloorAndObstacleTiles(type, x, y);
        } else {
            throw new IllegalArgumentException("Unknown tile type: " + type);
        }

        return tile;
    }


    private Tile createFloorAndObstacleTiles(int type, int x, int y) {
        final Tile tile = new Tile(type, x, y);

        // "Floor" tiles and obstacles
        tile.setObstacle(type > 10);

        return tile;
    }
}
