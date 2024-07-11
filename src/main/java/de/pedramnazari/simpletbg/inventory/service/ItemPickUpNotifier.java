package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.ArrayList;
import java.util.List;

public class ItemPickUpNotifier implements IItemPickUpNotifier {
    private final List<IItemPickUpListener> itemPickUpListeners = new ArrayList<>();

    @Override
    public void addItemPickupListener(IItemPickUpListener listener) {
        itemPickUpListeners.add(listener);
    }

    @Override
    public void notifyItemPickedUp(ICharacter element, IItem item) {
        for (IItemPickUpListener listener : itemPickUpListeners) {
            listener.onItemPickedUp(element, item);
        }
    }
}
