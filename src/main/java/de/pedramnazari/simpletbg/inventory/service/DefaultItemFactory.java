package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.IItemFactory;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;

public class DefaultItemFactory extends AbstractTileMapElementFactory<Item> implements IItemFactory {

    public static final String ITEM_MAGIC_YELLOW_KEY_NAME = "Magic Yellow Key";
    public static final String ITEM_MAGIC_YELLOW_KEY_DESC = "A yellow key that opens the door to the next level.";

    public static final String ITEM_MAGIC_YELLOW_KEY2_NAME = "Magic Yellow Key";
    public static final String ITEM_MAGIC_YELLOW_KEY2_DESC = "A yellow key that opens the door to the next level.";

    @Override
    protected Item createNonEmptyElement(int type, int x, int y) {
        String itemName;
        String itemDescription;

        if (type == TileType.ITEM_YELLOW_KEY.getType()) {
            itemName = ITEM_MAGIC_YELLOW_KEY_NAME;
            itemDescription = ITEM_MAGIC_YELLOW_KEY_DESC;
        }
        else if (type == TileType.ITEM_YELLOW_KEY2.getType()) {
                itemName = ITEM_MAGIC_YELLOW_KEY2_NAME;
                itemDescription = ITEM_MAGIC_YELLOW_KEY2_DESC;
        }
        else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }

        return new Item(x, y, itemName, itemDescription, type);
    }
}
