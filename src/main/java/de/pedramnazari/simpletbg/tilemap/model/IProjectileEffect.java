package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Collection;

/**
 * Interface for projectile on-hit effects.
 * Effects are applied when a projectile hits an enemy.
 */
public interface IProjectileEffect {
    /**
     * Apply the effect when a projectile hits an enemy.
     * 
     * @param hitEnemy The enemy that was hit
     * @param allEnemies All enemies in the game (for chain effects)
     * @return Collection of additional enemies affected by this effect (e.g., chain targets)
     */
    Collection<IEnemy> apply(IEnemy hitEnemy, Collection<IEnemy> allEnemies);
}
