package de.pedramnazari.simpletbg.character.hero.model;

import org.junit.jupiter.api.Test;

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
}
