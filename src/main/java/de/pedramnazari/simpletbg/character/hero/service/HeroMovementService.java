package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.inventory.model.Bomb;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IItemService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementService;

import java.util.Collection;
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
        Collection<? extends ITileMapElement> collidingEnemies = collisionDetectionService.getCollidingElements(element, gameContext.getEnemies());

        if (!collidingEnemies.isEmpty()) {
            logger.log(Level.INFO, "Collision with enemy detected at position: " + newX + ", " + newY);
            result.addCollidingElements(collidingEnemies);
        }
    }

    private void handleItems(IItemService itemService, IMovableTileElement element, int newX, int newY, MovementResult result) {
        if ((element instanceof IHero hero)) {
            final IItem item = itemService.getItem(newX, newY).orElse(null);
            if ((item != null) && !(item instanceof Bomb)) {
                logger.log(Level.INFO, "Found item: " + item.getName());

                result.setCollectedItem(item);
                result.setItemCollector(hero);
            }
        }
    }

    // TODO: remove this method
    public CollisionDetectionService getCollisionDetectionService() {
        return collisionDetectionService;
    }
}
