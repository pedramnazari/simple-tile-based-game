package de.pedramnazari.simpletbg.quest.model;

public class QuestObjective {
    private final String description;

    private boolean isCompleted = false;

    public QuestObjective(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
