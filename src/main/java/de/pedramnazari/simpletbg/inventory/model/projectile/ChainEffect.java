package de.pedramnazari.simpletbg.inventory.model.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

/**
 * Chain effect that damages one additional enemy adjacent to the hit target.
 * 
 * Selection Priority (deterministic):
 * 1. Enemies in the 4-neighborhood (up, down, left, right) only
 * 2. If multiple candidates exist, select the one with:
 *    - Lowest Y coordinate first (topmost)
 *    - Then lowest X coordinate (leftmost) as tiebreaker
 */
public class ChainEffect implements IProjectileEffect {

    @Override
    public Collection<IEnemy> apply(IEnemy hitEnemy, Collection<IEnemy> allEnemies) {
        // Find adjacent enemies in 4-neighborhood
        Optional<IEnemy> chainTarget = allEnemies.stream()
                .filter(enemy -> enemy != hitEnemy) // Don't chain to the same enemy
                .filter(enemy -> isAdjacent4Neighborhood(hitEnemy, enemy))
                .min(Comparator.comparingInt(IEnemy::getY)
                        .thenComparingInt(IEnemy::getX));
        
        // Return the chain target in a collection, or empty if no target found
        return chainTarget.map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    /**
     * Check if an enemy is in the 4-neighborhood (up, down, left, right) of the hit enemy.
     */
    private boolean isAdjacent4Neighborhood(IEnemy hitEnemy, IEnemy other) {
        int dx = Math.abs(hitEnemy.getX() - other.getX());
        int dy = Math.abs(hitEnemy.getY() - other.getY());
        
        // Adjacent in 4-neighborhood means exactly 1 tile away in one direction
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
}
