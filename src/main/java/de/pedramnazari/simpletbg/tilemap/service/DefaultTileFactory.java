package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.IItemFactory;
import de.pedramnazari.simpletbg.tilemap.model.ITileFactory;
import de.pedramnazari.simpletbg.tilemap.model.Tile;

// TODO: separate TileFactory for obstacles, items etc
public class DefaultTileFactory extends AbstractTileMapElementFactory<Tile> implements ITileFactory {

    private final IItemFactory itemFactory;

    public DefaultTileFactory(IItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }


    @Override
    protected Tile createNonEmptyElement(int type, int x, int y) {
        Tile tile;
        if (type < 100) {
            tile = createFloorAndObstacleTiles(type, x, y);
        } else if (type <= 200) {
            tile = createItemAndAddToTile(type, x, y);
        } else {
            throw new IllegalArgumentException("Unknown tile type: " + type);
        }

        return tile;
    }

    private Tile createItemAndAddToTile(int type, int x, int y) {
        final Tile tile = new Tile(type, x, y);
        IItem item = itemFactory.createElement(type, x, y);
        tile.setItem(item);
        return tile;
    }

    private Tile createFloorAndObstacleTiles(int type, int x, int y) {
        final Tile tile = new Tile(type, x, y);

        // "Floor" tiles and obstacles
        tile.setObstacle(type > 10);

        return tile;
    }
}
