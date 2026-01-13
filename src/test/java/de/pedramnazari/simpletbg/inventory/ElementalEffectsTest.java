package de.pedramnazari.simpletbg.inventory;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.inventory.model.projectile.ChainEffect;
import de.pedramnazari.simpletbg.inventory.model.projectile.FreezeEffect;
import de.pedramnazari.simpletbg.inventory.model.projectile.IceProjectile;
import de.pedramnazari.simpletbg.inventory.model.projectile.LightningProjectile;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileEffect;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ElementalEffectsTest {

    @Test
    public void testFreezeEffectSetsOneFrozenTurn() {
        // Given
        IEnemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        FreezeEffect freezeEffect = new FreezeEffect();
        
        // When
        freezeEffect.apply(enemy, List.of(enemy));
        
        // Then
        assertEquals(1, enemy.getFrozenTurns());
    }

    @Test
    public void testFreezeEffectDoesNotChain() {
        // Given
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        IEnemy nearbyEnemy = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);
        FreezeEffect freezeEffect = new FreezeEffect();
        
        // When
        Collection<IEnemy> chainTargets = freezeEffect.apply(hitEnemy, Arrays.asList(hitEnemy, nearbyEnemy));
        
        // Then
        assertTrue(chainTargets.isEmpty());
    }

    @Test
    public void testFrozenEnemyDecrementsAfterTurn() {
        // Given
        IEnemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        enemy.setFrozenTurns(1);
        
        // When
        int remaining = enemy.decrementFrozenTurns();
        
        // Then
        assertEquals(0, remaining);
        assertEquals(0, enemy.getFrozenTurns());
    }

    @Test
    public void testChainEffectSelectsAdjacentEnemy() {
        // Given
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        IEnemy rightEnemy = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);
        IEnemy farEnemy = new Enemy(TileType.ENEMY_LR.getType(), 10, 10);
        ChainEffect chainEffect = new ChainEffect();
        
        // When
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, Arrays.asList(hitEnemy, rightEnemy, farEnemy));
        
        // Then
        assertEquals(1, chainTargets.size());
        assertTrue(chainTargets.contains(rightEnemy));
    }

    @Test
    public void testChainEffectNoTargetWhenNoAdjacentEnemies() {
        // Given
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        IEnemy farEnemy = new Enemy(TileType.ENEMY_LR.getType(), 10, 10);
        ChainEffect chainEffect = new ChainEffect();
        
        // When
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, Arrays.asList(hitEnemy, farEnemy));
        
        // Then
        assertTrue(chainTargets.isEmpty());
    }

    @Test
    public void testChainEffectSelectsTopLeftWithPriority() {
        // Given: Multiple adjacent enemies
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        IEnemy topEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 4);     // Should be selected (lower Y)
        IEnemy bottomEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 6);
        IEnemy leftEnemy = new Enemy(TileType.ENEMY_LR.getType(), 4, 5);
        IEnemy rightEnemy = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);
        ChainEffect chainEffect = new ChainEffect();
        
        // When
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, topEnemy, bottomEnemy, leftEnemy, rightEnemy));
        
        // Then: Should select the topmost enemy (lowest Y coordinate)
        assertEquals(1, chainTargets.size());
        assertTrue(chainTargets.contains(topEnemy));
    }

    @Test
    public void testChainEffectSelectsLeftWhenSameRow() {
        // Given: Multiple adjacent enemies at same Y
        IEnemy hitEnemy = new Enemy(TileType.ENEMY_LR.getType(), 5, 5);
        IEnemy leftEnemy = new Enemy(TileType.ENEMY_LR.getType(), 4, 5);    // Should be selected (lower X)
        IEnemy rightEnemy = new Enemy(TileType.ENEMY_LR.getType(), 6, 5);
        ChainEffect chainEffect = new ChainEffect();
        
        // When
        Collection<IEnemy> chainTargets = chainEffect.apply(hitEnemy, 
            Arrays.asList(hitEnemy, leftEnemy, rightEnemy));
        
        // Then: Should select the leftmost enemy (lowest X coordinate)
        assertEquals(1, chainTargets.size());
        assertTrue(chainTargets.contains(leftEnemy));
    }

    @Test
    public void testIceProjectileHasEffect() {
        // Given
        IceProjectile projectile = new IceProjectile(0, 0, MoveDirection.RIGHT, 5, null, 10);
        
        // When
        IProjectileEffect effect = projectile.getEffect().orElse(null);
        
        // Then
        assertNotNull(effect);
        assertTrue(effect instanceof FreezeEffect);
    }

    @Test
    public void testLightningProjectileHasEffect() {
        // Given
        LightningProjectile projectile = new LightningProjectile(0, 0, MoveDirection.RIGHT, 5, null, 10);
        
        // When
        IProjectileEffect effect = projectile.getEffect().orElse(null);
        
        // Then
        assertNotNull(effect);
        assertTrue(effect instanceof ChainEffect);
    }

    @Test
    public void testIceProjectileType() {
        // Given
        IceProjectile projectile = new IceProjectile(0, 0, MoveDirection.RIGHT, 5, null, 10);
        
        // Then
        assertEquals(TileType.PROJECTILE_ICE.getType(), projectile.getType());
    }

    @Test
    public void testLightningProjectileType() {
        // Given
        LightningProjectile projectile = new LightningProjectile(0, 0, MoveDirection.RIGHT, 5, null, 10);
        
        // Then
        assertEquals(TileType.PROJECTILE_LIGHTNING.getType(), projectile.getType());
    }
}
