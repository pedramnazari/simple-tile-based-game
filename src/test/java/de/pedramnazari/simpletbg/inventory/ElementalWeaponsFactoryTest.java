package de.pedramnazari.simpletbg.inventory;

import de.pedramnazari.simpletbg.inventory.model.IceWand;
import de.pedramnazari.simpletbg.inventory.model.LightningRod;
import de.pedramnazari.simpletbg.inventory.model.projectile.IceProjectileFactory;
import de.pedramnazari.simpletbg.inventory.model.projectile.LightningProjectileFactory;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElementalWeaponsFactoryTest {

    private DefaultItemFactory itemFactory;

    @BeforeEach
    public void setUp() {
        itemFactory = new DefaultItemFactory();
        itemFactory.setIceProjectileFactory(new IceProjectileFactory());
        itemFactory.setLightningProjectileFactory(new LightningProjectileFactory());
    }

    @Test
    public void testCreateIceWand() {
        // When
        IItem item = itemFactory.createElement(TileType.WEAPON_ICE_WAND.getType(), 5, 5);

        // Then
        assertNotNull(item);
        assertTrue(item instanceof IceWand);
        assertEquals(TileType.WEAPON_ICE_WAND.getType(), item.getType());
        assertEquals(5, item.getX());
        assertEquals(5, item.getY());
    }

    @Test
    public void testCreateLightningRod() {
        // When
        IItem item = itemFactory.createElement(TileType.WEAPON_LIGHTNING_ROD.getType(), 3, 7);

        // Then
        assertNotNull(item);
        assertTrue(item instanceof LightningRod);
        assertEquals(TileType.WEAPON_LIGHTNING_ROD.getType(), item.getType());
        assertEquals(3, item.getX());
        assertEquals(7, item.getY());
    }

    @Test
    public void testIceWandCreatesIceProjectile() {
        // Given
        IceWand iceWand = (IceWand) itemFactory.createElement(TileType.WEAPON_ICE_WAND.getType(), 0, 0);

        // Then
        assertNotNull(iceWand);
        assertEquals(5, iceWand.getRange());
        assertEquals(12, iceWand.getAttackingDamage());
    }

    @Test
    public void testLightningRodCreatesLightningProjectile() {
        // Given
        LightningRod lightningRod = (LightningRod) itemFactory.createElement(TileType.WEAPON_LIGHTNING_ROD.getType(), 0, 0);

        // Then
        assertNotNull(lightningRod);
        assertEquals(5, lightningRod.getRange());
        assertEquals(10, lightningRod.getAttackingDamage());
    }
}
