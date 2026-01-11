package de.pedramnazari.simpletbg.character.hero.model;

import de.pedramnazari.simpletbg.character.hero.model.spell.FireballSpell;
import de.pedramnazari.simpletbg.character.hero.model.spell.HealSpell;
import de.pedramnazari.simpletbg.character.hero.model.spell.Spell;
import de.pedramnazari.simpletbg.character.hero.model.spell.TeleportSpell;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Sorcerer extends Hero {

    private static final Logger logger = Logger.getLogger(Sorcerer.class.getName());
    private static final int MAX_SORCERER_MANA = 150;
    private static final int SORCERER_ATTACKING_POWER = 15;

    private final List<Spell> spells;

    public Sorcerer(int x, int y) {
        super(x, y);
        setMaxMana(MAX_SORCERER_MANA);
        increaseMana(MAX_SORCERER_MANA);
        setAttackingPower(SORCERER_ATTACKING_POWER);
        
        // Initialize sorcerer spells
        this.spells = new ArrayList<>();
        this.spells.add(new FireballSpell());
        this.spells.add(new HealSpell());
        this.spells.add(new TeleportSpell());
        
        logger.info("Sorcerer created with " + spells.size() + " spells: " + getSpellNames());
    }

    public List<Spell> getSpells() {
        return new ArrayList<>(spells);
    }

    /**
     * Cast a spell by index if enough mana is available
     * @param spellIndex the index of the spell to cast (0-2)
     * @return true if spell was cast successfully, false otherwise
     */
    public boolean castSpell(int spellIndex) {
        if (spellIndex < 0 || spellIndex >= spells.size()) {
            logger.warning("Invalid spell index: " + spellIndex);
            return false;
        }

        Spell spell = spells.get(spellIndex);
        if (!spell.canCast(getMana())) {
            logger.info("Not enough mana to cast " + spell.getName() + ". Required: " + spell.getManaCost() + ", Available: " + getMana());
            return false;
        }

        decreaseMana(spell.getManaCost());
        logger.info("Cast " + spell.getName() + "! Mana: " + getMana() + "/" + getMaxMana());
        return true;
    }

    /**
     * Cast fireball spell (index 0)
     */
    public boolean castFireball() {
        return castSpell(0);
    }

    /**
     * Cast heal spell (index 1) and restore health
     */
    public boolean castHeal() {
        if (castSpell(1)) {
            HealSpell healSpell = (HealSpell) spells.get(1);
            increaseHealth(healSpell.getHealingAmount());
            logger.info("Healed for " + healSpell.getHealingAmount() + " HP. Health: " + getHealth());
            return true;
        }
        return false;
    }

    /**
     * Cast teleport spell (index 2)
     */
    public boolean castTeleport() {
        return castSpell(2);
    }

    private String getSpellNames() {
        return String.join(", ", spells.stream().map(Spell::getName).toList());
    }
}

