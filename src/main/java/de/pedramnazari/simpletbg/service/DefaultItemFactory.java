package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.IItemFactory;
import de.pedramnazari.simpletbg.model.Item;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultItemFactory implements IItemFactory {

    @Override
    public Item createItem(int type, int x, int y) {
        if (type == 0) {
            // TODO: refactor
            return null;
        }

        String itemName;
        String itemDescription;

        if (type == 100) {
            itemName = DefaultTileFactory.ITEM_MAGIC_BLACK_KEY_NAME;
            itemDescription = DefaultTileFactory.ITEM_MAGIC_BLACK_KEY_DESC;
        } else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }

        return new Item(x, y, itemName, itemDescription);
    }

    @Override
    public Collection<Item> createItemsUsingTileMapConfig(TileMapConfig mapConfig) {
        final Collection<Item> items = new ArrayList<>();

        final int[][] map = mapConfig.getMap();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                final Item item = createItem(map[row][col], col, row);
                if (item != null) {
                    items.add(item);
                }
            }
        }

        return items;
    }
}
