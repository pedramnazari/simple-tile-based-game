package de.pedramnazari.simpletbg.quest.model;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    private final String name;
    private final String description;
    private final List<QuestObjective> objectives = new ArrayList<>();

    public Quest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addObjective(QuestObjective objective) {
        this.objectives.add(objective);
    }

    public List<Object> getObjectives() {
        return List.copyOf(objectives);
    }
}
