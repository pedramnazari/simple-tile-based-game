package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.IItemService;
import de.pedramnazari.simpletbg.inventory.service.ItemPickUpNotifier;
import de.pedramnazari.simpletbg.service.CollisionDetectionService;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tile.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.tile.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tile.service.navigation.MovementService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeroMovementService extends MovementService {

    private static final Logger logger = Logger.getLogger(HeroMovementService.class.getName());

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();
    private final CollisionDetectionService collisionDetectionService;

    public HeroMovementService(CollisionDetectionService collisionDetectionService) {
        this.collisionDetectionService = collisionDetectionService;
    }

    public void addItemPickupListener(IItemPickUpListener itemPickUpListener) {
        itemPickUpNotifier.addItemPickupListener(itemPickUpListener);
    }

    @Override
    protected void handleElementHasMoved(GameContext gameContext, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        handleItems(gameContext.getItemService(), element, newX, newY, result);
        handleCollisionsWithEnemies(gameContext, element, newX, newY, result);
    }

    private void handleCollisionsWithEnemies(GameContext gameContext, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        Collection<? extends IMoveableTileElement> collidingEnemies = collisionDetectionService.getCollidingElements(element, gameContext.getEnemies());

        if (!collidingEnemies.isEmpty()) {
            logger.log(Level.INFO, "Collision with enemy detected at position: " + newX + ", " + newY);
            result.addCollidingElements(collidingEnemies);
        }
    }

    private void handleItems(IItemService itemService, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        // TODO: Refactor (do not use instanceof).
        if ((element instanceof Hero hero)) {
            final Optional<Item> optItem = itemService.getItem(newX, newY);
            if (optItem.isPresent()) {
                final Item item = optItem.get();

                logger.log(Level.INFO, "Found item: " + item.getName());
                hero.getInventory().addItem(item);

                // TODO: MovementService should not remove the item from the list of items directly (but via method call of the "owner").
                itemService.removeItem(item);

                itemPickUpNotifier.notifyItemPickedUp(hero, item, newX, newY);

                result.setCollectedItem(item);
            }
        }
    }
}
