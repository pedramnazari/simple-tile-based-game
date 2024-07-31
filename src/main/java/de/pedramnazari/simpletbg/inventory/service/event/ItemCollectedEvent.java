package de.pedramnazari.simpletbg.inventory.service.event;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

public record ItemCollectedEvent(ICharacter character, IItem item) {

}
