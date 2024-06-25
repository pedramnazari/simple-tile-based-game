package de.pedramnazari.simpletbg.model;

import java.util.Collection;

public interface IEnemyFactory {

    Enemy createEnemy(int type, int x, int y);
    Collection<Enemy> createEnemies(int[][] enemyMapConfig);


}
