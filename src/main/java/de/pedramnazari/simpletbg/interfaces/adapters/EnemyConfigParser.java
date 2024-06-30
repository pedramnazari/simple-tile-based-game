package de.pedramnazari.simpletbg.interfaces.adapters;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;

import java.util.Collection;

public class EnemyConfigParser {

    public Collection<Enemy> parse(int[][] enemyConfig, IEnemyFactory factory) {
        return factory.createElementsUsingTileMapConfig(enemyConfig);
    }
}
