package de.pedramnazari.simpletbg.character.hero.model;

import de.pedramnazari.simpletbg.character.hero.model.spell.Spell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SorcererTest {

    @Test
    void sorcerer_hasHigherManaAndAttackingPower() {
        Sorcerer sorcerer = new Sorcerer(5, 10);
        
        assertEquals(5, sorcerer.getX());
        assertEquals(10, sorcerer.getY());
        assertEquals(150, sorcerer.getMaxMana());
        assertEquals(150, sorcerer.getMana());
        assertEquals(15, sorcerer.getAttackingPower());
        assertEquals(100, sorcerer.getHealth());
    }

    @Test
    void sorcerer_canUseMana() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        int manaAfterUse = sorcerer.decreaseMana(30);
        assertEquals(120, manaAfterUse);
        assertEquals(120, sorcerer.getMana());
    }

    @Test
    void sorcerer_canRegenerateMana() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        sorcerer.decreaseMana(50);
        int manaAfterRegen = sorcerer.increaseMana(30);
        assertEquals(130, manaAfterRegen);
        assertEquals(130, sorcerer.getMana());
    }

    @Test
    void sorcerer_manaDoesNotExceedMaximum() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        int manaAfterRegen = sorcerer.increaseMana(100);
        assertEquals(150, manaAfterRegen);
        assertEquals(150, sorcerer.getMana());
    }

    @Test
    void sorcerer_manaDoesNotGoBelowZero() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        int manaAfterUse = sorcerer.decreaseMana(200);
        assertEquals(0, manaAfterUse);
        assertEquals(0, sorcerer.getMana());
    }

    @Test
    void sorcerer_hasThreeSpells() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        List<Spell> spells = sorcerer.getSpells();
        assertEquals(3, spells.size());
        assertEquals("Fireball", spells.get(0).getName());
        assertEquals("Heal", spells.get(1).getName());
        assertEquals("Teleport", spells.get(2).getName());
    }

    @Test
    void sorcerer_canCastFireball() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        boolean castSuccess = sorcerer.castFireball();
        assertTrue(castSuccess);
        assertEquals(120, sorcerer.getMana()); // 150 - 30 = 120
    }

    @Test
    void sorcerer_canCastHeal() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        sorcerer.decreaseHealth(50); // Reduce health to 50
        
        boolean castSuccess = sorcerer.castHeal();
        assertTrue(castSuccess);
        assertEquals(110, sorcerer.getMana()); // 150 - 40 = 110
        assertEquals(100, sorcerer.getHealth()); // 50 + 50 = 100 (capped at max)
    }

    @Test
    void sorcerer_canCastTeleport() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        
        boolean castSuccess = sorcerer.castTeleport();
        assertTrue(castSuccess);
        assertEquals(100, sorcerer.getMana()); // 150 - 50 = 100
    }

    @Test
    void sorcerer_cannotCastSpellWithInsufficientMana() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        sorcerer.decreaseMana(130); // Leave only 20 mana
        
        boolean castSuccess = sorcerer.castFireball(); // Requires 30 mana
        assertFalse(castSuccess);
        assertEquals(20, sorcerer.getMana()); // Mana unchanged
    }

    @Test
    void sorcerer_spellsHaveCorrectManaCosts() {
        Sorcerer sorcerer = new Sorcerer(0, 0);
        List<Spell> spells = sorcerer.getSpells();
        
        assertEquals(30, spells.get(0).getManaCost()); // Fireball
        assertEquals(40, spells.get(1).getManaCost()); // Heal
        assertEquals(50, spells.get(2).getManaCost()); // Teleport
    }
}
