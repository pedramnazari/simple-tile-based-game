package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

// TODO: separate TileFactory for obstacles, items etc
public class DefaultTileFactory extends AbstractTileMapElementFactory<Tile> implements ITileFactory {

    public DefaultTileFactory() {
    }


    @Override
    protected Tile createNonEmptyElement(int type, int x, int y) {
        Tile tile;
        if (type < 100) {
            tile = createFloorAndObstacleTiles(type, x, y);
        }
        else if (type == TileType.WALL_HIDING_PORTAL.getType()) {
            tile = new Tile(type, x, y);
            tile.setObstacle(true);
            tile.setDestructible(true);
            tile.setHitPoints(2);
            tile.setTransformToNewTileType(TileType.PORTAL.getType());
        }
        else if (type == TileType.WALL_HIDING_EXIT.getType()) {
            tile = new Tile(type, x, y);
            tile.setObstacle(true);
            tile.setDestructible(true);
            tile.setHitPoints(2);
            tile.setTransformToNewTileType(TileType.EXIT.getType());
        }
        else if (type == TileType.PORTAL.getType()) {
            tile = new Tile(type, x, y);
            tile.setPortal(true);
        }
        else if (type == TileType.EXIT.getType()) {
            tile = new Tile(type, x, y);
        }
        else {
            throw new IllegalArgumentException("Unknown tile type: " + type);
        }

        return tile;
    }


    private Tile createFloorAndObstacleTiles(int type, int x, int y) {
        final Tile tile = new Tile(type, x, y);

        // "Floor" tiles and obstacles
        tile.setObstacle(type >= TileType.WALL.getType());

        if (type >= 75) {
            if (type == TileType.WALL.getType()) {
                tile.setDestructible(false);
            }
            else if (type == TileType.DESTRUCTIBLE_WALL.getType()) {
                tile.setDestructible(true);
                tile.setHitPoints(2);
            }
        }

        return tile;
    }
}
