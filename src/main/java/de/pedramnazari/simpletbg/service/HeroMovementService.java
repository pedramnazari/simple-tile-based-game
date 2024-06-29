package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.model.Item;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeroMovementService extends MovementService {

    private static final Logger logger = Logger.getLogger(HeroMovementService.class.getName());

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();

    public void addItemPickupListener(IItemPickUpListener itemPickUpListener) {
        itemPickUpNotifier.addItemPickupListener(itemPickUpListener);
    }

    @Override
    protected void handleItems(IItemService itemService, IMoveableTileElement element, int newX, int newY, MovementResult result) {
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
