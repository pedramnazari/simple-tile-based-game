package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemServiceMock implements IItemService {

    @Override
    public boolean removeItem(Item item) {
        return false;
    }

    @Override
    public Collection<Item> getItems() {
        return List.of();
    }

    @Override
    public Optional<Item> getItem(int x, int y) {
        return Optional.empty();
    }
}
