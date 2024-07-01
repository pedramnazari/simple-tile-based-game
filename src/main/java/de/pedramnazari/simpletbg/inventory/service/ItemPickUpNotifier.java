package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPickUpNotifier {
    private List<IItemPickUpListener> itemPickUpListeners = new ArrayList<>();

    public void addItemPickupListener(IItemPickUpListener listener) {
        itemPickUpListeners.add(listener);
    }

    public void notifyItemPickedUp(IItemCollector element, Item item, int itemX, int itemY) {
        for (IItemPickUpListener listener : itemPickUpListeners) {
            listener.onItemPickedUp(element, item, itemX, itemY);
        }
    }
}
