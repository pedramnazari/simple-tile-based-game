package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemServiceMock extends ItemService {

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
