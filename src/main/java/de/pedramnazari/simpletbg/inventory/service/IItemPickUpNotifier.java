package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;

public interface IItemPickUpNotifier {

    void addItemPickupListener(IItemPickUpListener listener);

    void notifyItemPickedUp(IItemCollector element, Item item);
}
