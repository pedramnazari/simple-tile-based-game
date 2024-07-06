package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.Item;

public interface IItemPickUpListener {

    void onItemPickedUp(Character element, Item item);

}
