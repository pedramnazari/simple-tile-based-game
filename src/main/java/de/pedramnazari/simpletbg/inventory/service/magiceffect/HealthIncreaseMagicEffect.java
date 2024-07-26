package de.pedramnazari.simpletbg.inventory.service.magiceffect;

import de.pedramnazari.simpletbg.inventory.model.AbstractMagicEffect;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;

public class HealthIncreaseMagicEffect extends AbstractMagicEffect {
    private final int healthIncrease;

    public HealthIncreaseMagicEffect(int healthIncrease) {
        super("Health Increase Magic Power");
        this.healthIncrease = healthIncrease;
    }

    @Override
    public void apply(ICharacter character) {
        character.increaseHealth(healthIncrease);
    }

}
