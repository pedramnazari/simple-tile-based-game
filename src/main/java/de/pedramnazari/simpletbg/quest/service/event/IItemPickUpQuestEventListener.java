package de.pedramnazari.simpletbg.quest.service.event;

import de.pedramnazari.simpletbg.quest.model.IQuestEventListener;

public interface IItemPickUpQuestEventListener extends IQuestEventListener<ItemPickUpQuestEvent> {

    @Override
    void onEvent(ItemPickUpQuestEvent event);
}
