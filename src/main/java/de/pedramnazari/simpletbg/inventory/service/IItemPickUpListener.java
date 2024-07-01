package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;

public interface IItemPickUpListener {

    void onItemPickedUp(IItemCollector element, Item item, int itemX, int itemY);

}
