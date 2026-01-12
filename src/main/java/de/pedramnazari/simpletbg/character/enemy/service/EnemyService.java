package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.service.IWeaponDealsDamageListener;
import de.pedramnazari.simpletbg.inventory.service.event.*;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.HeroHitNotifier;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.IHeroHitListener;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyService implements IEnemyService, IEnemySubject, IHeroAttackListener, IWeaponDealsDamageListener {
    private final Logger logger = Logger.getLogger(EnemyService.class.getName());

    private final HeroHitNotifier heroHitNotifier = new HeroHitNotifier();

    private final Collection<IItemEventListener> itemEventListeners = new ArrayList<>();

    private final EnemyMovementService enemyMovementService;

    private final Collection<IEnemy> enemies = new ArrayList<>();
    private boolean initialized = false;

    private final List<IEnemyObserver> observers = new ArrayList<>();
    final EnemyHitNotifier enemyHitNotifier = new EnemyHitNotifier();

    public EnemyService(EnemyMovementService enemyMovementService) {
        this.enemyMovementService = enemyMovementService;
    }

    @Override
    public void init(Collection<IEnemy> enemies) {
        if (initialized) {
            throw new IllegalStateException("Enemies already initialized");
        }

        this.enemies.addAll(enemies);
        initialized = true;
    }

    @Override
    public List<MovementResult> moveEnemies(final GameContext gameContext) {
        final List<MovementResult> movementResults = new ArrayList<>();

        for (IEnemy enemy : enemies) {
            // Check if enemy is frozen
            if (enemy.getFrozenTurns() > 0) {
                enemy.decrementFrozenTurns();
                // Enemy skips its turn - create an empty movement result
                MovementResult frozenResult = new MovementResult();
                frozenResult.setOldX(enemy.getX());
                frozenResult.setOldY(enemy.getY());
                frozenResult.setNewX(enemy.getX());
                frozenResult.setNewY(enemy.getY());
                frozenResult.setHasElementMoved(false);
                movementResults.add(frozenResult);
                continue;
            }

            final Point newPosition = enemyMovementService.calcNextMove(gameContext.getTileMap(), enemy);

            final MovementResult result = enemyMovementService
                    .moveElementToPositionWithinMap(gameContext, enemy, newPosition.getX(), newPosition.getY());

            handleItemIfCollected(result);

            if (!result.getCollidingElements().isEmpty()) {
                // TODO: Move to HeroService (or CollisionService)
                // Assumption for now:
                // Enemies do not collide with each other.
                // Enemies can only collide with the hero.
                // So the colliding element is always the hero.
                final IHero hero = (IHero) result.getCollidingElements().iterator().next();

                heroHitNotifier.notifyHeroHit(hero, enemy, enemy.getAttackingPower());

            }

            movementResults.add(result);
        }

        if (!movementResults.isEmpty()) {
            notifyObservers();
        }

        return movementResults;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final IItem item = result.getCollectedItem().get();

            notifyItemCollected(new ItemCollectedEvent(result.getItemCollector().get(), item));
        }
    }

    @Override
    public Collection<IEnemy> getEnemies() {
        return List.copyOf(enemies);
    }

    @Override
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

    public void addEnemyHitListener(IEnemyHitListener listener) {
        enemyHitNotifier.addListener(listener);
    }

    @Override
    public void onHeroAttacksCharacter(final ICharacter attackCharacter, int damage) {
        if (!(attackCharacter instanceof IEnemy enemy)) {
            return;
        }

        final int newHealth = enemy.decreaseHealth(damage);

        logger.log(Level.INFO, "Hero attacks enemy. Health: {0}", enemy.getHealth());

        enemyHitNotifier.notifyEnemyHit(enemy, damage);

        if (newHealth == 0) {
            enemies.remove(enemy);

            logger.log(Level.INFO, "Enemy defeated. Remaining enemies: {0}", enemies.size());

            enemyHitNotifier.notifyEnemyDefeated(enemy);

            if (enemies.isEmpty()) {
                enemyHitNotifier.notifyAllEnemiesDefeated();
            }
        }

    }

    public void addHeroHitListener(IHeroHitListener listener) {
        heroHitNotifier.addListener(listener);
    }

    @Override
    public void onWeaponDealsDamage(IWeapon weapon, ICharacter attackedCharacter, int damage) {
        onHeroAttacksCharacter(attackedCharacter, damage);
    }

    public void addItemEventListener(IItemEventListener listener) {
        itemEventListeners.add(listener);
    }

    private void notifyItemCollected(ItemCollectedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemCollected(event);
        }
    }

    private void notifyItemEquipped(ItemEquippedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemEquipped(event);
        }
    }

    private void notifyItemAddedToInventory(ItemAddedToInventoryEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemAddedToInventory(event);
        }
    }

    private void notifyItemConsumed(ItemConsumedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemUsed(event);
        }
    }
}
