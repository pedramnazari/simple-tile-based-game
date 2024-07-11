package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.IItem;

import java.util.Collection;
import java.util.Optional;

public interface IItemService {

    boolean removeItem(IItem item);

    Collection<IItem> getItems();

    Optional<IItem> getItem(int x, int y);

}
