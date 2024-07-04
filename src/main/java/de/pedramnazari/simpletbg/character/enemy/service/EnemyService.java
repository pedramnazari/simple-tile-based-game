package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.model.IEnemyFactory;
import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpNotifier;
import de.pedramnazari.simpletbg.inventory.service.ItemPickUpNotifier;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyService implements IEnemySubject, IItemPickUpNotifier, IHeroAttackListener {
    private final Logger logger = Logger.getLogger(EnemyService.class.getName());

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();

    private final IEnemyFactory enemyFactory;
    private final EnemyMovementService enemyMovementService;

    private final Collection<Enemy> enemies = new ArrayList<>();
    private boolean initialized = false;

    private final List<IEnemyObserver> observers = new ArrayList<>();
    final EnemyHitNotifier enemyHitNotifier = new EnemyHitNotifier();

    public EnemyService(IEnemyFactory enemyFactory, EnemyMovementService enemyMovementService) {
        this.enemyFactory = enemyFactory;
        this.enemyMovementService = enemyMovementService;
    }

    public void init(Collection<Enemy> enemies) {
        if (initialized) {
            throw new IllegalStateException("Enemies already initialized");
        }

        this.enemies.addAll(enemies);
        initialized = true;
    }

    public List<MovementResult> moveEnemiesRandomlyWithinMap(final GameContext gameContext) {
        final List<MovementResult> movementResults = new ArrayList<>();

        for (Enemy enemy : enemies) {
            final Point newPosition = enemyMovementService.calcNextMove(gameContext.getTileMap(), enemy);

            final MovementResult result = enemyMovementService
                    .moveElementToPositionWithinMap(gameContext, enemy, newPosition.getX(), newPosition.getY());

            handleItemIfCollected(result);

            movementResults.add(result);
        }

        if (!movementResults.isEmpty()) {
            notifyObservers();
        }
        else {
            logger.log(Level.INFO, "No enemies moved");
        }

        return movementResults;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final Item item = result.getCollectedItem().get();

            itemPickUpNotifier.notifyItemPickedUp(result.getItemCollector().get(), item);
        }
    }

    public Collection<Enemy> getEnemies() {
        return List.copyOf(enemies);
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void registerObserver(IEnemyObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IEnemyObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // TODO: currently, we notify all observers about all enemies
        //       In future, we could notify only about the enemies that have changed
        for (IEnemyObserver observer : observers) {
            observer.update(List.copyOf(enemies));
        }
    }

    public EnemyMovementService getEnemyMovementService() {
        return enemyMovementService;
    }

    public void attackEnemy(Enemy enemy, int damage) {
        onHeroAttacksCharacter(enemy, damage);
    }

    public void addEnemyHitListener(IEnemyHitListener listener) {
        enemyHitNotifier.addListener(listener);
    }

    public void notifyEnemyHit(Enemy enemy, int damage) {
        enemyHitNotifier.notifyEnemyHit(enemy, damage);
    }

    @Override
    public void addItemPickupListener(IItemPickUpListener itemPickUpListener) {
        itemPickUpNotifier.addItemPickupListener(itemPickUpListener);
    }

    @Override
    public void notifyItemPickedUp(IItemCollector element, Item item) {
        itemPickUpNotifier.notifyItemPickedUp(element, item);
    }

    @Override
    public void onHeroAttacksCharacter(final Character character, int damage) {
        if (!(character instanceof Enemy enemy)) {
            return;
        }

        int newHealth = Math.max(0, enemy.getHealth() - damage);
        enemy.setHealth(newHealth);

        logger.log(Level.INFO, "Hero attacks enemy. Health: {0}", enemy.getHealth());

        enemyHitNotifier.notifyEnemyHit(enemy, damage);

        if (newHealth == 0) {
            enemies.remove(enemy);
            logger.log(Level.INFO, "Enemy defeated. Remaining enemies: {0}", enemies.size());
        }

    }
}
