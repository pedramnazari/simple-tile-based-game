package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;

public interface IEnemyHitListener {

    void onEnemyHit(IEnemy enemy, int damage);

    void onEnemyDefeated(IEnemy enemy);

    void onAllEnemiesDefeated();
}
