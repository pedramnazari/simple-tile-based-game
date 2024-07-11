package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.IQuestEventListener;

public interface IAllEnemiesDefeatedQuestEventListener extends IQuestEventListener<AllEnemiesDefeatedQuestEvent> {

    @Override
    void onEvent(AllEnemiesDefeatedQuestEvent event);
}
