package de.pedramnazari.simpletbg.quest.model;

import de.pedramnazari.simpletbg.quest.service.IQuestEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Quest {

    private static final Logger logger = Logger.getLogger(Quest.class.getName());

    private final String name;
    private final String description;
    private final List<QuestObjective> objectives = new ArrayList<>();

    private final Map<Class<? extends IQuestEvent>, List<IQuestEventListener<? extends IQuestEvent>>> listeners;

    public Quest(String name, String description) {
        this.name = name;
        this.description = description;

        listeners = new HashMap<>();
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

    public boolean isCompleted() {
        return objectives.stream().allMatch(QuestObjective::isCompleted);
    }

}
