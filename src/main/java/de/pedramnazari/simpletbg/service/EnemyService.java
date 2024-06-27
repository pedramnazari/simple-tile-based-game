package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;
import java.util.logging.Logger;

public class EnemyService {
    private final Logger logger = Logger.getLogger(EnemyService.class.getName());

    private final IEnemyFactory enemyFactory;
    private final EnemyMovementService enemyMovementService;

    private final Collection<Enemy> enemies = new ArrayList<>();
    private boolean initialized = false;

    public EnemyService(IEnemyFactory enemyFactory, EnemyMovementService enemyMovementService) {
        this.enemyFactory = enemyFactory;
        this.enemyMovementService = enemyMovementService;
    }

    public void init(TileMapConfig enemyMapConfig) {
        if (initialized) {
            throw new IllegalStateException("Enemies already initialized");
        }

        enemies.addAll(enemyFactory.createElementsUsingTileMapConfig(enemyMapConfig));
        initialized = true;
    }

    public List<MovementResult> moveEnemiesRandomlyWithinMap(Collection<Enemy> enemies, final TileMap tileMap, final Collection<Item> items) {
        final List<MovementResult> movementResults = new ArrayList<>();

        for (Enemy enemy : enemies) {
            final Set<Point> validPositions = enemyMovementService.calcValidMovePositionsWithinMap(tileMap, enemy);

            // add current position (i.e., enemy does not move)
            validPositions.add(new Point(enemy.getX(), enemy.getY()));

            final List<Point> list = new ArrayList<>(validPositions);
            final Random random = new Random();
            int randomIndex = random.nextInt(list.size());

            final Point newPosition = list.get(randomIndex);

            final MovementResult result = enemyMovementService
                    .moveElementToPositionWithinMap(tileMap, items, enemy, newPosition.getX(), newPosition.getY());

            movementResults.add(result);
        }

        return movementResults;
    }

    public Collection<Enemy> getEnemies() {
        return List.copyOf(enemies);
    }

    public boolean isInitialized() {
        return initialized;
    }
}
