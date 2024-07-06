package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.quest.model.IQuestEvent;

public class ItemPickUpQuestEvent implements IQuestEvent {
    private final Item collectedItem;
    private final Character collectingCharacter;

    public ItemPickUpQuestEvent(Character collectingCharacter, Item collectedItem) {
        this.collectedItem = collectedItem;
        this.collectingCharacter = collectingCharacter;
    }

    public Item getCollectedItem() {
        return collectedItem;
    }

    public Character getCollectingCharacter() {
        return collectingCharacter;
    }
}
