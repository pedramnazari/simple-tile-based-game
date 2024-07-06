package de.pedramnazari.simpletbg.quest.service;

public interface IAllEnemiesDefeatedQuestEventListener extends IQuestEventListener<AllEnemiesDefeatedQuestEvent> {

    @Override
    void onEvent(AllEnemiesDefeatedQuestEvent event);
}
