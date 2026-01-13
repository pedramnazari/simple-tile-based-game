package de.pedramnazari.simpletbg.inventory.model.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileEffect;

import java.util.Collection;
import java.util.Comparator;

/**
 * Chain effect that damages all enemies within range of the hit target.
 * Creates a branching lightning effect that spreads to nearby enemies.
 * 
 * Selection Criteria:
 * 1. All enemies within CHAIN_RANGE tiles (Manhattan distance)
 * 2. Excludes the originally hit enemy
 * 3. Returns enemies sorted by distance (closest first) for visual effect ordering
 */
public class ChainEffect implements IProjectileEffect {

    private static final int CHAIN_RANGE = 3;

    @Override
    public Collection<IEnemy> apply(IEnemy hitEnemy, Collection<IEnemy> allEnemies) {
        // Find all enemies within chain range and sort by distance
        return allEnemies.stream()
                .filter(enemy -> enemy != hitEnemy) // Don't chain to the same enemy
                .filter(enemy -> isWithinChainRange(hitEnemy, enemy))
                .sorted(Comparator.<IEnemy>comparingInt(enemy -> manhattanDistance(hitEnemy, enemy))
                        .thenComparingInt(IEnemy::getY)
                        .thenComparingInt(IEnemy::getX))
                .toList();
    }

    /**
     * Check if an enemy is within chain range using Manhattan distance.
     */
    private boolean isWithinChainRange(IEnemy hitEnemy, IEnemy other) {
        return manhattanDistance(hitEnemy, other) <= CHAIN_RANGE;
    }

    /**
     * Calculate Manhattan distance between two enemies.
     */
    private int manhattanDistance(IEnemy enemy1, IEnemy enemy2) {
        return Math.abs(enemy1.getX() - enemy2.getX()) + Math.abs(enemy1.getY() - enemy2.getY());
    }
}
