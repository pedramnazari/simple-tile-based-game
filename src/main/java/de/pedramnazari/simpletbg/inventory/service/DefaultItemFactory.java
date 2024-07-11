package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.model.Weapon;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.IItemFactory;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;

public class DefaultItemFactory extends AbstractTileMapElementFactory<IItem> implements IItemFactory {

    public static final String ITEM_MAGIC_YELLOW_KEY_NAME = "Magic Yellow Key";
    public static final String ITEM_MAGIC_YELLOW_KEY_DESC = "A yellow key that opens the door to the next level.";

    public static final String ITEM_MAGIC_YELLOW_KEY2_NAME = "Magic Yellow Key";
    public static final String ITEM_MAGIC_YELLOW_KEY2_DESC = "A yellow key that opens the door to the next level.";

    @Override
    protected IItem createNonEmptyElement(int type, int x, int y) {
        IItem item;
        String itemName;
        String itemDescription;

        // TODO: use weapon factory for weapon type

        if (type == TileType.ITEM_YELLOW_KEY.getType()) {
            itemName = ITEM_MAGIC_YELLOW_KEY_NAME;
            itemDescription = ITEM_MAGIC_YELLOW_KEY_DESC;
            item = new Item(x, y, itemName, itemDescription, type);
        }
        else if (type == TileType.ITEM_YELLOW_KEY2.getType()) {
            itemName = ITEM_MAGIC_YELLOW_KEY2_NAME;
            itemDescription = ITEM_MAGIC_YELLOW_KEY2_DESC;
            item = new Item(x, y, itemName, itemDescription, type);
        }
        else if (type == TileType.WEAPON_SWORD.getType()) {
            itemName = "Sword";
            itemDescription = "A sword that can be used to fight enemies.";
            final IWeapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(20);

            item = weapon;
        }
        else if (type == TileType.WEAPON_SWORD2.getType()) {
            itemName = "Black Sword";
            itemDescription = "A strong black sword that can be used to fight enemies.";
            final IWeapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(100);

            item = weapon;
        }
        else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }

        return item;
    }
}
