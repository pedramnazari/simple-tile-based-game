package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.service.IItemService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemServiceMock implements IItemService {

    @Override
    public boolean removeItem(IItem item) {
        return false;
    }

    @Override
    public void addItems(Collection<IItem> items) {

    }

    @Override
    public void addItem(IItem item) {

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
