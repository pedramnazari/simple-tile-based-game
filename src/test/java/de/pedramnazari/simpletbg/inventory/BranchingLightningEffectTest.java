package de.pedramnazari.simpletbg.inventory;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.inventory.model.projectile.ChainEffect;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the branching lightning effect that chains to multiple enemies within range.
 * This demonstrates the new modern UI feature where lightning splits into branching bolts.
 */
public class BranchingLightningEffectTest {

    @Test
    public void testLightningBranchesToMultipleEnemiesInRange() {
        // Given: A hit enemy surrounded by multiple enemies within chain range (3 tiles)
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        
        // All these enemies are within range 3 (Manhattan distance)
        IEnemy enemy1 = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);   // Distance 1
        IEnemy enemy2 = new Enemy(TileType.ENEMY_LR.getType(), 5, 6);   // Distance 1
        IEnemy enemy3 = new Enemy(TileType.ENEMY_LR.getType(), 4, 5);   // Distance 1
        IEnemy enemy4 = new Enemy(TileType.ENEMY_LR.getType(), 5, 4);   // Distance 1
        IEnemy enemy5 = new Enemy(TileType.ENEMY_LR.getType(), 7, 5);   // Distance 2
        IEnemy enemy6 = new Enemy(TileType.ENEMY_LR.getType(), 5, 7);   // Distance 2
        IEnemy enemy7 = new Enemy(TileType.ENEMY_LR.getType(), 6, 6);   // Distance 2
        
        ChainEffect chainEffect = new ChainEffect();
        
        // When: Chain effect is applied
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, enemy1, enemy2, enemy3, enemy4, enemy5, enemy6, enemy7));
        
        // Then: All enemies within range should be chained (branching effect)
        assertEquals(7, chainTargets.size(), "Lightning should branch to all 7 enemies within range");
        assertTrue(chainTargets.contains(enemy1));
        assertTrue(chainTargets.contains(enemy2));
        assertTrue(chainTargets.contains(enemy3));
        assertTrue(chainTargets.contains(enemy4));
        assertTrue(chainTargets.contains(enemy5));
        assertTrue(chainTargets.contains(enemy6));
        assertTrue(chainTargets.contains(enemy7));
    }

    @Test
    public void testLightningDoesNotChainToEnemiesBeyondRange() {
        // Given: A hit enemy with some enemies within range and some beyond
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        
        IEnemy nearEnemy1 = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);   // Distance 1 - within range
        IEnemy nearEnemy2 = new Enemy(TileType.ENEMY_LR.getType(), 7, 6);   // Distance 3 - within range
        IEnemy farEnemy1 = new Enemy(TileType.ENEMY_LR.getType(), 9, 5);    // Distance 4 - beyond range
        IEnemy farEnemy2 = new Enemy(TileType.ENEMY_LR.getType(), 5, 10);   // Distance 5 - beyond range
        
        ChainEffect chainEffect = new ChainEffect();
        
        // When: Chain effect is applied
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, nearEnemy1, nearEnemy2, farEnemy1, farEnemy2));
        
        // Then: Only enemies within range 3 should be chained
        assertEquals(2, chainTargets.size(), "Lightning should only branch to enemies within range 3");
        assertTrue(chainTargets.contains(nearEnemy1));
        assertTrue(chainTargets.contains(nearEnemy2));
        assertFalse(chainTargets.contains(farEnemy1));
        assertFalse(chainTargets.contains(farEnemy2));
    }

    @Test
    public void testLightningChainsSortedByDistance() {
        // Given: Multiple enemies at different distances
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        
        IEnemy enemy1 = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);   // Distance 1
        IEnemy enemy2 = new Enemy(TileType.ENEMY_LR.getType(), 7, 6);   // Distance 3
        IEnemy enemy3 = new Enemy(TileType.ENEMY_LR.getType(), 5, 7);   // Distance 2
        
        ChainEffect chainEffect = new ChainEffect();
        
        // When: Chain effect is applied
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, enemy1, enemy2, enemy3));
        
        // Then: Enemies should be sorted by distance (closest first) for visual effect ordering
        List<IEnemy> targetList = chainTargets.stream().toList();
        assertEquals(3, targetList.size());
        assertEquals(enemy1, targetList.get(0), "Closest enemy (distance 1) should be first");
        assertEquals(enemy3, targetList.get(1), "Second closest enemy (distance 2) should be second");
        assertEquals(enemy2, targetList.get(2), "Furthest enemy (distance 3) should be last");
    }

    @Test
    public void testLightningChainsToAllNearbyEnemiesCreatingBranchingPattern() {
        // Given: A realistic scenario with enemies positioned around the hit point
        // This simulates the visual branching pattern where lightning spreads out
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 10, 10);
        
        // Create a cluster of enemies around the hit point
        IEnemy north = new Enemy(TileType.ENEMY_LR.getType(), 10, 8);     // Distance 2
        IEnemy south = new Enemy(TileType.ENEMY_LR.getType(), 10, 12);    // Distance 2
        IEnemy east = new Enemy(TileType.ENEMY_LR.getType(), 12, 10);     // Distance 2
        IEnemy west = new Enemy(TileType.ENEMY_LR.getType(), 8, 10);      // Distance 2
        IEnemy northeast = new Enemy(TileType.ENEMY_LR.getType(), 11, 9); // Distance 2
        IEnemy northwest = new Enemy(TileType.ENEMY_LR.getType(), 9, 9);  // Distance 2
        IEnemy southeast = new Enemy(TileType.ENEMY_LR.getType(), 11, 11);// Distance 2
        IEnemy southwest = new Enemy(TileType.ENEMY_LR.getType(), 9, 11); // Distance 2
        
        ChainEffect chainEffect = new ChainEffect();
        
        // When: Chain effect is applied
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, north, south, east, west, northeast, northwest, southeast, southwest));
        
        // Then: All surrounding enemies should be hit by branching lightning bolts
        assertEquals(8, chainTargets.size(), "Lightning should branch to all 8 surrounding enemies");
        
        // This creates a visual pattern where lightning branches out in all directions
        // Each enemy will receive a visual traveling lightning bolt from the hit point
        assertTrue(chainTargets.containsAll(Arrays.asList(north, south, east, west, northeast, northwest, southeast, southwest)),
            "All directional enemies should be in the branching pattern");
    }

    @Test
    public void testNoInfiniteChaining() {
        // Given: Original hit enemy should not be included in chain targets
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        IEnemy nearbyEnemy = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);
        
        ChainEffect chainEffect = new ChainEffect();
        
        // When: Chain effect is applied
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, nearbyEnemy));
        
        // Then: The original hit enemy should not be in the chain targets
        assertFalse(chainTargets.contains(hitEnemy), "Hit enemy should not chain to itself");
        assertTrue(chainTargets.contains(nearbyEnemy), "Nearby enemy should be in chain targets");
    }
}
