package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;

import java.util.Collection;

public class EnemyService {
    private final IEnemyFactory enemyFactory;

    public EnemyService(IEnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
    }

    public Collection<Enemy> createEnemies(int[][] enemyMapConfig) {
        return enemyFactory.createEnemies(enemyMapConfig);
    }


}
