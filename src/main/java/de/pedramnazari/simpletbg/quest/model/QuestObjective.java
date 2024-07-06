package de.pedramnazari.simpletbg.quest.model;

public class QuestObjective {
    private final String description;

    private boolean isCompleted = false;
    private boolean goalDefeatAllEnemies = false;

    public QuestObjective(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setGoalDefeatAllEnemies(boolean goalDefeatAllEnemies) {
        this.goalDefeatAllEnemies = goalDefeatAllEnemies;
    }

    public boolean isGoalDefeatAllEnemies() {
        return goalDefeatAllEnemies;
    }

    public void complete() {
        isCompleted = true;
    }

}
