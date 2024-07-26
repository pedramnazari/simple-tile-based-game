package de.pedramnazari.simpletbg.quest.service.config;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.service.QuestService;

public abstract class AbstractQuestConfig implements IQuestConfig {
    private final String questId;
    protected Quest quest;
    protected QuestService questService;

    public AbstractQuestConfig(String questId) {
        this.questId = questId;
    }

    @Override
    public Quest getQuest() {
        return quest;
    }

    @Override
    public QuestService getQuestEventDispatcher() {
        return questService;
    }
}
