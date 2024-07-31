package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

public interface IItemPickUpListener {

    void onItemPickedUp(ICharacter character, IItem item);

}
