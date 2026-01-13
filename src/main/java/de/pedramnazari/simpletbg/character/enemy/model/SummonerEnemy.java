package de.pedramnazari.simpletbg.character.enemy.model;

import de.pedramnazari.simpletbg.character.model.Character;

public class SummonerEnemy extends Character implements de.pedramnazari.simpletbg.tilemap.model.IEnemy {

    private int frozenTurns = 0;
    private int turnsSinceLastSpawn = 0;
    private static final int SPAWN_INTERVAL = 5; // Spawn every 5 turns
    private long spawnSeed = 0;

    public SummonerEnemy(int type, int x, int y) {
        super(type, x, y);
    }

    @Override
    public void setFrozenTurns(int turns) {
        this.frozenTurns = Math.max(0, turns);
    }

    @Override
    public int getFrozenTurns() {
        return frozenTurns;
    }

    @Override
    public int decrementFrozenTurns() {
        if (frozenTurns > 0) {
            frozenTurns--;
        }
        return frozenTurns;
    }

    /**
     * Checks if it's time to spawn a rush creature.
     * @return true if should spawn, false otherwise
     */
    public boolean shouldSpawn() {
        turnsSinceLastSpawn++;
        if (turnsSinceLastSpawn >= SPAWN_INTERVAL) {
            turnsSinceLastSpawn = 0;
            return true;
        }
        return false;
    }

    /**
     * Gets the next spawn seed for creating rush creatures with unique behavior.
     * @return spawn seed
     */
    public long getNextSpawnSeed() {
        return spawnSeed++;
    }
}
