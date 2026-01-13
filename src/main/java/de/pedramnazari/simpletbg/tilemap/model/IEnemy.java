package de.pedramnazari.simpletbg.tilemap.model;

public interface IEnemy extends ICharacter, IItemCollector {
    /**
     * Sets the number of turns this enemy should be frozen (skip movement).
     * @param turns Number of turns to freeze (0 = not frozen)
     */
    void setFrozenTurns(int turns);

    /**
     * Gets the number of remaining frozen turns.
     * @return Number of turns remaining (0 = not frozen)
     */
    int getFrozenTurns();

    /**
     * Decrements the frozen turn counter by 1 if frozen.
     * @return The new frozen turn count
     */
    int decrementFrozenTurns();
}
