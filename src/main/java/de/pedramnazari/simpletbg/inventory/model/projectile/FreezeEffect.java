package de.pedramnazari.simpletbg.inventory.model.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileEffect;

import java.util.Collection;
import java.util.Collections;

/**
 * Freeze effect that causes an enemy to skip exactly one turn.
 */
public class FreezeEffect implements IProjectileEffect {

    @Override
    public Collection<IEnemy> apply(IEnemy hitEnemy, Collection<IEnemy> allEnemies) {
        // Apply freeze: enemy skips exactly its next turn
        hitEnemy.setFrozenTurns(1);
        
        // Freeze effect doesn't chain, so return empty collection
        return Collections.emptyList();
    }
}
