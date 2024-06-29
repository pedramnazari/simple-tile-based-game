package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyMovementService extends MovementService {

    final static Logger logger = Logger.getLogger(EnemyMovementService.class.getName());

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();
    private final IMovementStrategy movementStrategy;
    private final CollisionDetectionService collisionDetectionService;

    public EnemyMovementService(IMovementStrategy movementStrategy, CollisionDetectionService collisionDetectionService) {
        this.movementStrategy = movementStrategy;
        this.collisionDetectionService = collisionDetectionService;
    }

    public void addItemPickupListener(IItemPickUpListener listener) {
        itemPickUpNotifier.addItemPickupListener(listener);
    }

    @Override
    protected void handleElementHasMoved(GameContext gameContext, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        handleItems(gameContext.getItemService(), element, newX, newY, result);
        handleCollisionsWithHero(gameContext, element, newX, newY, result);
    }

    private void handleCollisionsWithHero(GameContext gameContext, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        final Hero hero = gameContext.getHero();
        if (collisionDetectionService.isCollision(element, hero)) {
            logger.log(Level.INFO, "Collision with hero detected at position: " + newX + ", " + newY);
            result.addCollidingElement(hero);
        }
    }

    private void handleItems(IItemService itemService, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        // TODO: Refactor (do not use instanceof).
        if ((element instanceof Enemy enemy)) {
            final Optional<Item> optItem = itemService.getItem(newX, newY);
            if (optItem.isPresent()) {
                final Item item = optItem.get();

                logger.log(Level.INFO, "Enemy collected item: " + item.getName());

                // TODO: MovementService should not remove the item from the list of items directly (but via method call of the "owner").
                itemService.removeItem(item);

                itemPickUpNotifier.notifyItemPickedUp(enemy, item, newX, newY);

                result.setCollectedItem(item);
            }
        }
    }

    public Point calcNextMove(TileMap tileMap, IMoveableTileElement element) {
        return movementStrategy.calcNextMove(tileMap, element);
    }
}
