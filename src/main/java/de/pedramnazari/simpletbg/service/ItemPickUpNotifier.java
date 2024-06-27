package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.IItemCollectorElement;
import de.pedramnazari.simpletbg.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPickUpNotifier {
    private List<IItemPickUpListener> itemPickUpListeners = new ArrayList<>();

    public void addItemPickupListener(IItemPickUpListener listener) {
        itemPickUpListeners.add(listener);
    }

    public void notifyItemPickedUp(IItemCollectorElement element, Item item, int itemX, int itemY) {
        for (IItemPickUpListener listener : itemPickUpListeners) {
            listener.onItemPickedUp(element, item, itemX, itemY);
        }
    }
}
