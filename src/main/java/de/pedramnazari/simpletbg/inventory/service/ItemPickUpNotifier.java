package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPickUpNotifier implements IItemPickUpNotifier {
    private final List<IItemPickUpListener> itemPickUpListeners = new ArrayList<>();

    @Override
    public void addItemPickupListener(IItemPickUpListener listener) {
        itemPickUpListeners.add(listener);
    }

    @Override
    public void notifyItemPickedUp(Character element, Item item) {
        for (IItemPickUpListener listener : itemPickUpListeners) {
            listener.onItemPickedUp(element, item);
        }
    }
}
