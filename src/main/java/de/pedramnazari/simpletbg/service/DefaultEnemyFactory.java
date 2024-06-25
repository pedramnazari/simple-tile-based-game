package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultEnemyFactory implements IEnemyFactory {

    @Override
    public Enemy createEnemy(int type, int x, int y) {
        final Enemy enemy;
        if (type == 1) {
            enemy = new Enemy(x, y);
        } else {
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        return enemy;
    }

    @Override
    public Collection<Enemy> createEnemies(int[][] enemyMapConfig) {
        final Collection<Enemy> enemies = new ArrayList<>();

        for (int row = 0; row < enemyMapConfig.length; row++) {
            for (int col = 0; col < enemyMapConfig[row].length; col++) {
                final int type = enemyMapConfig[row][col];
                if (type != 0) {
                    Enemy enemy = this.createEnemy(type, col, row);
                    enemies.add(enemy);
                }
            }
        }

        return enemies;
    }

}
