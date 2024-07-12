package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.QuestObjective;
import de.pedramnazari.simpletbg.quest.service.event.IItemPickUpQuestEventListener;
import de.pedramnazari.simpletbg.quest.service.event.ItemPickUpQuestEvent;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.logging.Logger;

public class ItemPickUpQuestObjective extends QuestObjective implements IItemPickUpQuestEventListener {

    private static final Logger logger = Logger.getLogger(ItemPickUpQuestObjective.class.getName());

    public ItemPickUpQuestObjective(String description) {
        super(description);
    }


    @Override
    public void onEvent(ItemPickUpQuestEvent event) {
        IItem item = event.getCollectedItem();

        ICharacter character = event.getCollectingCharacter();

        if (character instanceof IHero hero) {
            // TODO: use constant for item name
            if (item.getName().equals("Black Sword")) {
                logger.info("ItemPickUpQuestObjective: Black Sword picked up");
                complete();
            }
        }


    }
}
