package de.pedramnazari.simpletbg.quest.model;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyHitListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Quest implements IEnemyHitListener {

    private static final Logger logger = Logger.getLogger(Quest.class.getName());

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

    public boolean isCompleted() {
        return objectives.stream().allMatch(QuestObjective::isCompleted);
    }

    @Override
    public void onEnemyHit(Enemy enemy, int damage) {

    }

    @Override
    public void onEnemyDefeated(Enemy enemy) {

    }

    private void checkIfQuestIsCompleted() {
        if (isCompleted()) {
            logger.info("Quest '" + name + "' completed");
        }
    }

    @Override
    public void onAllEnemiesDefeated() {
        for (QuestObjective objective : objectives) {
            if (objective.isGoalDefeatAllEnemies()) {
                objective.complete();
            }
        }

        checkIfQuestIsCompleted();
    }
}
