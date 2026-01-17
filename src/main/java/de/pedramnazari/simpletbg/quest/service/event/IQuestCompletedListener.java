package de.pedramnazari.simpletbg.quest.service.event;

/**
 * Listener interface for quest completion events.
 */
public interface IQuestCompletedListener {
    
    /**
     * Called when a quest is completed.
     */
    void onQuestCompleted();
}
