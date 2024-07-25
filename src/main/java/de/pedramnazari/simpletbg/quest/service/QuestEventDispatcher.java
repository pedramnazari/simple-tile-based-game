package de.pedramnazari.simpletbg.quest.service;

import de.pedramnazari.simpletbg.character.enemy.service.IEnemyHitListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.quest.model.IQuestEvent;
import de.pedramnazari.simpletbg.quest.model.IQuestEventListener;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.service.event.AllEnemiesDefeatedQuestEvent;
import de.pedramnazari.simpletbg.quest.service.event.ItemPickUpQuestEvent;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.ICharacterMovedToSpecialTileListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuestEventDispatcher implements IEnemyHitListener, IItemPickUpListener, ICharacterMovedToSpecialTileListener {
    private static final Logger logger = Logger.getLogger(QuestEventDispatcher.class.getName());

    private final Map<Class<? extends IQuestEvent>, List<IQuestEventListener<? extends IQuestEvent>>> listeners = new HashMap<>();
    private final Quest quest;

    public QuestEventDispatcher(Quest quest) {
        this.quest = quest;
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

            if (quest.isCompleted()) {
                logger.info("Dispatch: Quest completed");
                //dispatch(new QuestCompletedEvent());
            }
        }
    }

    @Override
    public void onEnemyHit(IEnemy enemy, int damage) {

    }

    @Override
    public void onEnemyDefeated(IEnemy enemy) {

    }

    @Override
    public void onAllEnemiesDefeated() {
        logger.info("Dispatch: All enemies defeated");
        dispatch(new AllEnemiesDefeatedQuestEvent());
    }

    @Override
    public void onItemPickedUp(ICharacter element, IItem item) {
        logger.info("Dispatch: Item picked up");
        dispatch(new ItemPickUpQuestEvent(element, item));
    }

    public Quest getQuest() {
        return quest;
    }

    @Override
    public void onCharacterMovedToSpecialTile(ICharacter character, Tile specialTile) {
        if ((character instanceof IHero) && (specialTile.getType() == (TileType.EXIT.getType()))) {
            logger.info("Dispatch: Quest completed and Hero moved to exit");
        }
    }
}

