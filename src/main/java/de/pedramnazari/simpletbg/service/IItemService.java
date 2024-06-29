package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface IItemService {

    boolean removeItem(Item item);

    Collection<Item> getItems();

    Optional<Item> getItem(int x, int y);

}
