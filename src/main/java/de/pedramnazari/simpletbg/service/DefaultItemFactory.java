package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.AbstractTileMapElementFactory;
import de.pedramnazari.simpletbg.model.IItemFactory;
import de.pedramnazari.simpletbg.model.Item;

public class DefaultItemFactory extends AbstractTileMapElementFactory<Item> implements IItemFactory {

    @Override
    protected Item createNonEmptyElement(int type, int x, int y) {
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
}
