package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.service.event.*;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.service.IItemService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemService implements IItemService, IItemEventListener {

    private final Collection<IItem> items = new ArrayList<>();

    @Override
    public Collection<IItem> getItems() {
        return List.copyOf(items);
    }

    @Override
    public Optional<IItem> getItem(final int x, final int y) {
        for (IItem item : items) {
            if ((item.getX() == x) && (item.getY() == y)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    @Deprecated
    public boolean removeItem(IItem item) {
        return items.remove(item);
    }

    @Override
    public void addItems(Collection<IItem> items) {
        this.items.addAll(items);
    }

    @Override
    public void addItem(IItem item) {
        items.add(item);
    }

    @Override
    public void onItemCollected(ItemCollectedEvent event) {
        IItem item = event.item();

        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item not in list");
        }

        items.remove(item);
    }

    @Override
    public void onItemEquipped(ItemEquippedEvent event) {

    }

    @Override
    public void onItemAddedToInventory(ItemAddedToInventoryEvent event) {

    }

    @Override
    public void onItemUsed(ItemConsumedEvent event) {

    }
}
