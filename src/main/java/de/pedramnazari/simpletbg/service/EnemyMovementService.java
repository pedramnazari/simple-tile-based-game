package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.model.Item;
import de.pedramnazari.simpletbg.model.TileMap;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnemyMovementService extends MovementService {

    final static Logger logger = Logger.getLogger(EnemyMovementService.class.getName());

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();
    private final IMovementStrategy movementStrategy;

    // TODO: delete this constructor
    public EnemyMovementService() {
        this(new RandomMovementStrategy());
    }


    public EnemyMovementService(IMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public void addItemPickupListener(IItemPickUpListener listener) {
        itemPickUpNotifier.addItemPickupListener(listener);
    }

    @Override
    protected void handleItems(Collection<Item> items, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        // TODO: Refactor (do not use instanceof).
        if ((element instanceof Enemy enemy)) {
            final Optional<Item> optItem = getItem(items, newX, newY);
            if (optItem.isPresent()) {
                final Item item = optItem.get();

                logger.log(Level.INFO, "Enemy collected item: " + item.getName());


                // TODO: MovementService should not remove the item from the list of items directly (but via method call of the "owner").
                items.remove(item);

                itemPickUpNotifier.notifyItemPickedUp(enemy, item, newX, newY);

                result.setCollectedItem(item);
            }
        }
    }

    public Point calcNextMove(TileMap tileMap, IMoveableTileElement element) {
        return movementStrategy.calcNextMove(tileMap, element);
    }
}
