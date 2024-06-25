package de.pedramnazari.simpletbg.model;

import de.pedramnazari.simpletbg.service.TileMapConfig;

import java.util.Collection;

public interface IItemFactory {

    Item createItem(int type, int x, int y);


    Collection<Item> createItemsUsingTileMapConfig(TileMapConfig mapConfig);
}
