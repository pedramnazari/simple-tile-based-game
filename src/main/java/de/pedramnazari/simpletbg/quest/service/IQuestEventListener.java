package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.quest.model.IQuestEvent;

public interface IQuestEventListener<T extends IQuestEvent> {
    void onEvent(T event);
}
