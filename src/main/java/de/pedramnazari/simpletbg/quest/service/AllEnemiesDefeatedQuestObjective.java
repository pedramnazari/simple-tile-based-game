package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.model.QuestObjective;

public class AllEnemiesDefeatedQuestObjective extends QuestObjective implements IAllEnemiesDefeatedQuestEventListener{

    public AllEnemiesDefeatedQuestObjective(String description) {
        super(description);
    }

    @Override
    public void registerAsListener(Quest quest) {
        quest.registerListener(AllEnemiesDefeatedQuestEvent.class, this);
    }

    @Override
    public void onEvent(AllEnemiesDefeatedQuestEvent event) {
        complete();
    }
}
