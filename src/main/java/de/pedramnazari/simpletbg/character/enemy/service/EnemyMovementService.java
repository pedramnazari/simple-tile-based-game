package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyMovementService extends MovementService {

    final static Logger logger = Logger.getLogger(EnemyMovementService.class.getName());

    private final CollisionDetectionService collisionDetectionService;

    private final Collection<IMovementStrategy> movementStrategies;

    public EnemyMovementService(CollisionDetectionService collisionDetectionService) {
        this.collisionDetectionService = collisionDetectionService;

        this.movementStrategies = new ArrayList<>();
    }

    public boolean addMovementStrategy(IMovementStrategy strategy) {
        return movementStrategies.add(strategy);
    }

    public boolean removeMovementStrategy(IMovementStrategy strategy) {
        return movementStrategies.remove(strategy);
    }


    @Override
    protected void handleElementHasMoved(GameContext gameContext, IMovableTileElement element, int newX, int newY, MovementResult result) {
        //handleItems(gameContext.getItemService(), element, newX, newY, result);
        handleCollisionsWithHero(gameContext, element, newX, newY, result);
    }

    private void handleCollisionsWithHero(GameContext gameContext, IMovableTileElement element, int newX, int newY, MovementResult result) {
        final IHero hero = gameContext.getHero();
        if (collisionDetectionService.isCollision(element, hero)) {
            logger.log(Level.INFO, "Collision with hero detected at position: " + newX + ", " + newY);
            result.addCollidingElement(hero);
        }
    }

    private void handleItems(ItemService itemService, IMovableTileElement element, int newX, int newY, MovementResult result) {
        // TODO: Refactor (do not use instanceof).
        if ((element instanceof Enemy enemy)) {
            final Optional<IItem> optItem = itemService.getItem(newX, newY);
            if (optItem.isPresent()) {
                final IItem item = optItem.get();

                logger.log(Level.INFO, "Enemy collected item: " + item.getName() + " at position: " + newX + ", " + newY);

                result.setCollectedItem(item);
                result.setItemCollector(enemy);
            }
        }
    }

    public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
        final IMovementStrategy strategy = element.getMovementStrategy();
        return strategy.calcNextMove(tileMap, element);
    }
}
