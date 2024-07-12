package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.QuestObjective;
import de.pedramnazari.simpletbg.quest.service.event.AllEnemiesDefeatedQuestEvent;
import de.pedramnazari.simpletbg.quest.service.event.IAllEnemiesDefeatedQuestEventListener;

public class AllEnemiesDefeatedQuestObjective extends QuestObjective implements IAllEnemiesDefeatedQuestEventListener {

    public AllEnemiesDefeatedQuestObjective(String description) {
        super(description);
    }

    @Override
    public void onEvent(AllEnemiesDefeatedQuestEvent event) {
        complete();
    }
}
