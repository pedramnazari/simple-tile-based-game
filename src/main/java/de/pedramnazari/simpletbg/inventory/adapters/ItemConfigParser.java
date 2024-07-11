package de.pedramnazari.simpletbg.inventory.adapters;

import de.pedramnazari.simpletbg.inventory.service.IItemFactory;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.Collection;

public class ItemConfigParser {

    public Collection<IItem> parse(int[][] itemsConfig, IItemFactory factory) {
        return factory.createElementsUsingTileMapConfig(itemsConfig);
    }
}
