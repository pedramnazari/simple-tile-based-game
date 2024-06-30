package de.pedramnazari.simpletbg.interfaces.adapters;

import de.pedramnazari.simpletbg.model.IItemFactory;
import de.pedramnazari.simpletbg.model.Item;

import java.util.Collection;

public class ItemConfigParser {

    public Collection<Item> parse(int[][] itemsConfig, IItemFactory factory) {
        return factory.createElementsUsingTileMapConfig(itemsConfig);
    }
}
