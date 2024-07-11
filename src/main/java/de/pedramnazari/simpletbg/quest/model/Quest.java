package de.pedramnazari.simpletbg.quest.model;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.IEnemyHitListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.quest.service.AllEnemiesDefeatedQuestEvent;
import de.pedramnazari.simpletbg.quest.service.IQuestEventListener;
import de.pedramnazari.simpletbg.quest.service.ItemPickUpQuestEvent;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Quest implements IEnemyHitListener, IItemPickUpListener {

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
        objective.registerAsListener(this);
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
        logger.info("Quest: All enemies defeated");
        dispatch(new AllEnemiesDefeatedQuestEvent());
    }

    public <T extends IQuestEvent> void registerListener(Class<T> eventType, IQuestEventListener<T> listener) {
        this.listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T extends IQuestEvent> void dispatch(T event) {
        final List<IQuestEventListener<? extends IQuestEvent>> eventListeners = this.listeners.get(event.getClass());
        if (eventListeners != null) {
            for (IQuestEventListener<? extends IQuestEvent> listener : eventListeners) {
                ((IQuestEventListener<T>) listener).onEvent(event);
            }

            checkIfQuestIsCompleted();
        }
    }

    @Override
    public void onItemPickedUp(ICharacter element, IItem item) {
        logger.info("Quest: Item picked up");
        dispatch(new ItemPickUpQuestEvent(element, item));
    }
}
