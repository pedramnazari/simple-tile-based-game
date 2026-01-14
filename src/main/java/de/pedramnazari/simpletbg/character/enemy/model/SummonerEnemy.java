package de.pedramnazari.simpletbg.character.enemy.model;

public class SummonerEnemy extends Enemy {

    private int turnsSinceLastSpawn = 0;
    private static final int SPAWN_INTERVAL = 5; // Spawn every 5 turns
    private long spawnSeed = 0;

    public SummonerEnemy(int type, int x, int y) {
        super(type, x, y);
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
