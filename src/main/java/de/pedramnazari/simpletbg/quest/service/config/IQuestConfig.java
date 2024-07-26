package de.pedramnazari.simpletbg.quest.service.config;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.service.QuestService;

public interface IQuestConfig {

    Quest getQuest();
    QuestService getQuestEventDispatcher();
}
