package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.QuestObjective;

public class AllEnemiesDefeatedQuestObjective extends QuestObjective implements IAllEnemiesDefeatedQuestEventListener{

    public AllEnemiesDefeatedQuestObjective(String description) {
        super(description);
    }

    @Override
    public void onEvent(AllEnemiesDefeatedQuestEvent event) {
        complete();
    }
}
