package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

public interface IItemPickUpNotifier {

    void addItemPickupListener(IItemPickUpListener listener);

    void notifyItemPickedUp(ICharacter element, IItem item);
}
