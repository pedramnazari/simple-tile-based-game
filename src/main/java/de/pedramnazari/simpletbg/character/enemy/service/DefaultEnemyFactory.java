package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.model.IEnemyFactory;
import de.pedramnazari.simpletbg.tile.model.TileType;
import de.pedramnazari.simpletbg.tile.service.AbstractTileMapElementFactory;

public class DefaultEnemyFactory extends AbstractTileMapElementFactory<Enemy> implements IEnemyFactory {

    @Override
    protected Enemy createNonEmptyElement(int type, int x, int y) {
        final Enemy enemy;
        if (type == TileType.ENEMY.getType()) {
            enemy = new Enemy(x, y);
            System.out.println("Created enemy at " + x + ", " + y);
        } else {
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        return enemy;
    }

}
