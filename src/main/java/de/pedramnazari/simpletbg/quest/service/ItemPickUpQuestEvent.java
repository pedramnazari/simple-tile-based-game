package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.IQuestEvent;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

public class ItemPickUpQuestEvent implements IQuestEvent {
    private final IItem collectedItem;
    private final ICharacter collectingCharacter;

    public ItemPickUpQuestEvent(ICharacter collectingCharacter, IItem collectedItem) {
        this.collectedItem = collectedItem;
        this.collectingCharacter = collectingCharacter;
    }

    public IItem getCollectedItem() {
        return collectedItem;
    }

    public ICharacter getCollectingCharacter() {
        return collectingCharacter;
    }
}
