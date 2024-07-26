package de.pedramnazari.simpletbg.inventory.service.magiceffect;

import de.pedramnazari.simpletbg.inventory.model.AbstractMagicEffect;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;

import java.util.logging.Logger;

public class RandomEnemyDamageMagicEffect extends AbstractMagicEffect {

    private static final Logger logger = Logger.getLogger(RandomEnemyDamageMagicEffect.class.getName());


    public RandomEnemyDamageMagicEffect() {
        super(RandomEnemyDamageMagicEffect.class.getSimpleName());
    }

    @Override
    public void apply(ICharacter character) {

    }

}
