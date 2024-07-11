package de.pedramnazari.simpletbg.character.enemy.adapters;

import de.pedramnazari.simpletbg.character.enemy.service.IEnemyFactory;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;

import java.util.Collection;

public class EnemyConfigParser {

    public Collection<IEnemy> parse(int[][] enemyConfig, IEnemyFactory factory) {
        return factory.createElementsUsingTileMapConfig(enemyConfig);
    }
}
