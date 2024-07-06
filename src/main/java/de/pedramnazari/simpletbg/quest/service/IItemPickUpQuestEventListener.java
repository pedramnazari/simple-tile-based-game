package de.pedramnazari.simpletbg.quest.service;

public interface IItemPickUpQuestEventListener extends IQuestEventListener<ItemPickUpQuestEvent>{

    @Override
    void onEvent(ItemPickUpQuestEvent event);
}
