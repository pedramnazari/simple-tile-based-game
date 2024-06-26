package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.model.Item;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeroMovementService extends MovementService {

    private static final Logger logger = Logger.getLogger(HeroMovementService.class.getName());

    @Override
    protected void handleItems(Collection<Item> items, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        // TODO: Refactor (do not use instanceof).
        if ((element instanceof Hero)) {
            final Optional<Item> optItem = getItem(items, newX, newY);
            if (optItem.isPresent()) {
                final Item item = optItem.get();

                logger.log(Level.INFO, "Found item: " + item.getName());
                ((Hero) element).getInventory().addItem(item);

                // TODO: MovementService should not remove the item from the list of items directly (but via method call of the "owner").
                items.remove(item);

                result.setCollectedItem(item);
            }
        }
    }
}
