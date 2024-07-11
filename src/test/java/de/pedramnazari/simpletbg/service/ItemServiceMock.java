package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemServiceMock extends ItemService {

    @Override
    public boolean removeItem(IItem item) {
        return false;
    }

    @Override
    public Collection<IItem> getItems() {
        return List.of();
    }

    @Override
    public Optional<IItem> getItem(int x, int y) {
        return Optional.empty();
    }
}
