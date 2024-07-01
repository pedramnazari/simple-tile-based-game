package de.pedramnazari.simpletbg.tile.service.navigation;


import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.tile.model.ITileMapElement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MovementResult {
    private int oldX;
    private int oldY;
    private int newX;
    private int newY;
    private boolean hasElementMoved;
    private Item collectedItem;
    private final Set<ITileMapElement> collidingElements = new HashSet<>();

    // TODO: move outside this class?
    private String oldMapIndex;
    private String newMapIndex;

    public boolean hasElementMoved() {
        return hasElementMoved;
    }

    public void setHasElementMoved(boolean hasMoved) {
        this.hasElementMoved = hasMoved;
    }

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    public int getNewX() {
        return newX;
    }

    public void setNewX(int newX) {
        this.newX = newX;
    }

    public int getNewY() {
        return newY;
    }

    public void setNewY(int newY) {
        this.newY = newY;
    }

    public Item getCollectedItem() {
        return collectedItem;
    }

    public void setCollectedItem(Item collectedItem) {
        this.collectedItem = collectedItem;
    }

    public void setOldMapIndex(String oldMapIndex) {
        this.oldMapIndex = oldMapIndex;
    }

    public String getOldMapIndex() {
        return oldMapIndex;
    }

    public void setNewMapIndex(String newMapIndex) {
        this.newMapIndex = newMapIndex;
    }

    public String getNewMapIndex() {
        return newMapIndex;
    }

    public Set<ITileMapElement> getCollidingElements() {
        return Set.copyOf(collidingElements);
    }

    public void addCollidingElement(ITileMapElement element) {
        collidingElements.add(element);
    }

    public void addCollidingElements(Collection<? extends ITileMapElement> elements) {
        collidingElements.addAll(elements);
    }
}
