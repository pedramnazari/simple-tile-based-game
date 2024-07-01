package de.pedramnazari.simpletbg.character.enemy.adapters;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.model.IEnemyFactory;

import java.util.Collection;

public class EnemyConfigParser {

    public Collection<Enemy> parse(int[][] enemyConfig, IEnemyFactory factory) {
        return factory.createElementsUsingTileMapConfig(enemyConfig);
    }
}
