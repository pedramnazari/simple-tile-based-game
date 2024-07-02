package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.IItemFactory;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;

public class DefaultItemFactory extends AbstractTileMapElementFactory<Item> implements IItemFactory {

    @Override
    protected Item createNonEmptyElement(int type, int x, int y) {
        String itemName;
        String itemDescription;

        if (type == 100) {
            itemName = DefaultTileFactory.ITEM_MAGIC_YELLOW_KEY_NAME;
            itemDescription = DefaultTileFactory.ITEM_MAGIC_YELLOW_KEY_DESC;
        }
        else if (type == 101) {
                itemName = DefaultTileFactory.ITEM_MAGIC_YELLOW_KEY_NAME;
                itemDescription = DefaultTileFactory.ITEM_MAGIC_YELLOW_KEY_DESC;
        }
        else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }

        return new Item(x, y, itemName, itemDescription, type);
    }
}
