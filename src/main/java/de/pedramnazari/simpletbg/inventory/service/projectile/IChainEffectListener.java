package de.pedramnazari.simpletbg.inventory.service.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IProjectile;

/**
 * Listener for chain effect events (e.g., lightning chain)
 */
public interface IChainEffectListener {
    /**
     * Called when a projectile chains from one enemy to another
     * @param projectile The projectile that caused the chain
     * @param fromEnemy The enemy that was hit first
     * @param toEnemy The enemy that was hit by the chain
     */
    void onChainEffect(IProjectile projectile, IEnemy fromEnemy, IEnemy toEnemy);
}
