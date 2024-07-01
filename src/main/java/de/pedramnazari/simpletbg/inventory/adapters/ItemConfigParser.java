package de.pedramnazari.simpletbg.inventory.adapters;

import de.pedramnazari.simpletbg.inventory.model.IItemFactory;
import de.pedramnazari.simpletbg.inventory.model.Item;

import java.util.Collection;

public class ItemConfigParser {

    public Collection<Item> parse(int[][] itemsConfig, IItemFactory factory) {
        return factory.createElementsUsingTileMapConfig(itemsConfig);
    }
}
