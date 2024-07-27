package de.pedramnazari.simpletbg.inventory.service.magiceffect;

import de.pedramnazari.simpletbg.inventory.model.AbstractMagicEffect;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;

public class HealthModifierMagicEffect extends AbstractMagicEffect {
    private final int healthDelta;

    public HealthModifierMagicEffect(int healthDelta) {
        super("Health Modifier Magic Power");
        this.healthDelta = healthDelta;
    }

    @Override
    public void apply(ICharacter character) {
        if (healthDelta < 0) {
            character.decreaseHealth(-healthDelta);
        }
        else {
            character.increaseHealth(healthDelta);
        }
    }

}
