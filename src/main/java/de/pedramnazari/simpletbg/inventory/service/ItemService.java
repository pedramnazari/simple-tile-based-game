package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.inventory.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemService implements IItemPickUpListener {

    private final Collection<Item> items = new ArrayList<>();

    public Collection<Item> getItems() {
        return List.copyOf(items);
    }

    public Optional<Item> getItem(final int x, final int y) {
        for (Item item : items) {
            if ((item.getX() == x) && (item.getY() == y)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Deprecated
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public void addItems(Collection<Item> items) {
        this.items.addAll(items);
    }

    @Override
    public void onItemPickedUp(Character element, Item item) {
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item not in list");
        }

        items.remove(item);
    }
}
