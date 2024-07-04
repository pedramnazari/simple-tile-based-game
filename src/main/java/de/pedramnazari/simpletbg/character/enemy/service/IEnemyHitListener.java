package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;

public interface IEnemyHitListener {

    void onEnemyHit(Enemy enemy, int damage);

    void onEnemyDefeated(Enemy enemy);

    void onAllEnemiesDefeated();
}
