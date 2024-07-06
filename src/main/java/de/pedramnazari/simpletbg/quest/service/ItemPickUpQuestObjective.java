package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.model.QuestObjective;

import java.util.logging.Logger;

public class ItemPickUpQuestObjective extends QuestObjective implements IItemPickUpQuestEventListener {

    private static final Logger logger = Logger.getLogger(ItemPickUpQuestObjective.class.getName());

    public ItemPickUpQuestObjective(String description) {
        super(description);
    }

    @Override
    public void registerAsListener(Quest quest) {
        quest.registerListener(ItemPickUpQuestEvent.class, this);
    }


    @Override
    public void onEvent(ItemPickUpQuestEvent event) {
        Item item = event.getCollectedItem();

        Character character = event.getCollectingCharacter();

        if (character instanceof Hero hero) {
            // TODO: use constant for item name
            if (item.getName().equals("Black Sword")) {
                logger.info("ItemPickUpQuestObjective: Black Sword picked up");
                complete();
            }
        }


    }
}
