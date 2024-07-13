package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.AbstractMagicPower;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

import java.util.Collection;
import java.util.logging.Logger;

public class RandomEnemyDamageMagic extends AbstractMagicPower {

    private static final Logger logger = Logger.getLogger(RandomEnemyDamageMagic.class.getName());


    public RandomEnemyDamageMagic() {
        super(RandomEnemyDamageMagic.class.getSimpleName());
    }

    @Override
    public void activate(IHero hero, Collection<IEnemy> enemies) {
        logger.info("RandomEnemyDamageMagic activated");
    }
}
