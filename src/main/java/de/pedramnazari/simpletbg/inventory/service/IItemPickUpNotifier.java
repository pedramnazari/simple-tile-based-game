package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.Item;

public interface IItemPickUpNotifier {

    void addItemPickupListener(IItemPickUpListener listener);

    void notifyItemPickedUp(Character element, Item item);
}
