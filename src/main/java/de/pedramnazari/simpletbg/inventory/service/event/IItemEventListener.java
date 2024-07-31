package de.pedramnazari.simpletbg.inventory.service.event;

public interface IItemEventListener {
    void onItemCollected(ItemCollectedEvent event);
    void onItemEquipped(ItemEquippedEvent event);
    void onItemAddedToInventory(ItemAddedToInventoryEvent event);
    void onItemUsed(ItemConsumedEvent event);
}

