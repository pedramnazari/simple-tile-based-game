package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.IItemCollectorElement;
import de.pedramnazari.simpletbg.inventory.model.Item;

public interface IItemPickUpListener {

    void onItemPickedUp(IItemCollectorElement element, Item item, int itemX, int itemY);

}
