package de.pedramnazari.simpletbg.quest.model;

public interface IQuestEventListener<T extends IQuestEvent> {
    void onEvent(T event);
}
