package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.model.IEnemyFactory;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.character.service.HeroHitNotifier;
import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.character.service.IHeroHitListener;
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
    private final HeroHitNotifier heroHitNotifier = new HeroHitNotifier();

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

    public List<MovementResult> moveEnemies(final GameContext gameContext) {
        final List<MovementResult> movementResults = new ArrayList<>();

        for (Enemy enemy : enemies) {
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
                final Hero hero = (Hero) result.getCollidingElements().iterator().next();

                notifyHeroHit(hero, enemy, 0);

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
            logger.log(Level.INFO, "Enemy defeated. Remaining enemies: {0}", enemies.size());
            enemyHitNotifier.notifyEnemyDefeated(enemy);

            enemies.remove(enemy);

            if (enemies.isEmpty()) {
                enemyHitNotifier.notifyAllEnemiesDefeated();
            }
        }

    }

    public void addHeroHitListener(IHeroHitListener listener) {
        heroHitNotifier.addListener(listener);
    }

    public void notifyHeroHit(Hero hero, Character attackingCharacter, int damage) {
        heroHitNotifier.notifyHeroHit(hero, attackingCharacter, damage);
    }
}
