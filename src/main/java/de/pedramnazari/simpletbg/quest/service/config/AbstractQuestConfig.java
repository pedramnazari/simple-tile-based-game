package de.pedramnazari.simpletbg.quest.service.config;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.service.QuestEventDispatcher;

public abstract class AbstractQuestConfig implements IQuestConfig {
    private final String questId;
    protected Quest quest;
    protected QuestEventDispatcher questEventDispatcher;

    public AbstractQuestConfig(String questId) {
        this.questId = questId;
    }

    @Override
    public Quest getQuest() {
        return quest;
    }

    @Override
    public QuestEventDispatcher getQuestEventDispatcher() {
        return questEventDispatcher;
    }
}
