package de.pedramnazari.simpletbg.quest.model;

import java.util.logging.Logger;

public abstract class QuestObjective {
    private static final Logger logger = Logger.getLogger(QuestObjective.class.getName());

    private final String description;

    private boolean isCompleted = false;

    public QuestObjective(String description) {
        this.description = description;
    }

    public abstract void registerAsListener(Quest quest);

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete() {
        logger.info("QuestObjective: " + description + " completed");
        isCompleted = true;
    }
}
