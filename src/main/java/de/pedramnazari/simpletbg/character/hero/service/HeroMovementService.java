package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementService;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeroMovementService extends MovementService {

    private static final Logger logger = Logger.getLogger(HeroMovementService.class.getName());


    private final CollisionDetectionService collisionDetectionService;

    public HeroMovementService(CollisionDetectionService collisionDetectionService) {
        this.collisionDetectionService = collisionDetectionService;
    }

    @Override
    protected void handleElementHasMoved(GameContext gameContext, IMovableTileElement element, int newX, int newY, MovementResult result) {
        handleItems(gameContext.getItemService(), element, newX, newY, result);
        handleCollisionsWithEnemies(gameContext, element, newX, newY, result);
    }

    private void handleCollisionsWithEnemies(GameContext gameContext, IMovableTileElement element, int newX, int newY, MovementResult result) {
        Collection<? extends IMovableTileElement> collidingEnemies = collisionDetectionService.getCollidingElements(element, gameContext.getEnemies());

        if (!collidingEnemies.isEmpty()) {
            logger.log(Level.INFO, "Collision with enemy detected at position: " + newX + ", " + newY);
            result.addCollidingElements(collidingEnemies);
        }
    }

    private void handleItems(ItemService itemService, IMovableTileElement element, int newX, int newY, MovementResult result) {
        // TODO: Refactor (do not use instanceof).
        if ((element instanceof Hero hero)) {
            final Optional<Item> optItem = itemService.getItem(newX, newY);
            if (optItem.isPresent()) {
                final Item item = optItem.get();

                logger.log(Level.INFO, "Found item: " + item.getName());

                result.setCollectedItem(item);
                result.setItemCollector(hero);
            }
        }
    }
}
