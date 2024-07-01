package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface IItemService {

    boolean removeItem(Item item);

    Collection<Item> getItems();

    Optional<Item> getItem(int x, int y);

}
