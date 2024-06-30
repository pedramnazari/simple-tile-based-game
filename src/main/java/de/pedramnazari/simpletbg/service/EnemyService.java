package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyService implements IEnemySubject {
    private final Logger logger = Logger.getLogger(EnemyService.class.getName());

    private final IEnemyFactory enemyFactory;
    private final EnemyMovementService enemyMovementService;

    private final Collection<Enemy> enemies = new ArrayList<>();
    private boolean initialized = false;

    private final List<IEnemyObserver> observers = new ArrayList<>();

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
}
