package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.AbstractTileMapElementFactory;
import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;

public class DefaultEnemyFactory extends AbstractTileMapElementFactory<Enemy> implements IEnemyFactory {

    @Override
    protected Enemy createNonEmptyElement(int type, int x, int y) {
        final Enemy enemy;
        if (type == 1) {
            enemy = new Enemy(x, y);
            System.out.println("Created enemy at " + x + ", " + y);
        } else {
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        return enemy;
    }

}
