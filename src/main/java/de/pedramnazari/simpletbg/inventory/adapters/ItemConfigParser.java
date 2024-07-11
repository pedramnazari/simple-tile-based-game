package de.pedramnazari.simpletbg.inventory.adapters;

import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.IItemFactory;

import java.util.Collection;

public class ItemConfigParser {

    public Collection<IItem> parse(int[][] itemsConfig, IItemFactory factory) {
        return factory.createElementsUsingTileMapConfig(itemsConfig);
    }
}
