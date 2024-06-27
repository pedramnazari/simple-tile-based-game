package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.IItemCollectorElement;
import de.pedramnazari.simpletbg.model.Item;

public interface IItemPickUpListener {

    void onItemPickedUp(IItemCollectorElement element, Item item, int itemX, int itemY);

}
