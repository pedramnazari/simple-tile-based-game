package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.ITileFactory;
import de.pedramnazari.simpletbg.model.Item;
import de.pedramnazari.simpletbg.model.Tile;

// TODO: separate TileFactory for obstacles, items etc
public class DefaultTileFactory implements ITileFactory {

        @Override
        public Tile createTile(int type, int x, int y) {
            Tile tile;
            if (type < 100) {
                tile = createFloorAndObstacleTiles(type, x, y);
            }
            else if (type <= 200) {
                tile = createItemTile(type, x, y);
            }
            else {
                throw new IllegalArgumentException("Unknown tile type: " + type);
            }

            return tile;
        }

    private Tile createItemTile(int type, int x, int y) {
        final Tile tile = new Tile(type, x, y);

        String itemName;
        String itemDescription;

        if (type == 100) {
            itemName = "Magic Black Key";
            itemDescription = "A black key that opens the door to the next level.";
        }
        else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }

        final Item item = new Item(itemName, itemDescription);
        tile.setItem(item);

        return tile;
    }

    private Tile createFloorAndObstacleTiles(int type, int x, int y) {
        final Tile tile = new Tile(type, x, y);

        // "Floor" tiles and obstacles
        if (type <= 10) {
            tile.setObstacle(false);
        } else {
            tile.setObstacle(true);
        }

        return tile;
    }
}
