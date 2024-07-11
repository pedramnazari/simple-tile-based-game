package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemService implements IItemPickUpListener {

    private final Collection<IItem> items = new ArrayList<>();

    public Collection<IItem> getItems() {
        return List.copyOf(items);
    }

    public Optional<IItem> getItem(final int x, final int y) {
        for (IItem item : items) {
            if ((item.getX() == x) && (item.getY() == y)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Deprecated
    public boolean removeItem(IItem item) {
        return items.remove(item);
    }

    public void addItems(Collection<IItem> items) {
        this.items.addAll(items);
    }

    @Override
    public void onItemPickedUp(ICharacter element, IItem item) {
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item not in list");
        }

        items.remove(item);
    }
}
