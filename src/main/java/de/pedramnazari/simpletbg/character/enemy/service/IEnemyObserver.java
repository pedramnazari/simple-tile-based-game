package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;

import java.util.Collection;

public interface IEnemyObserver {

    void update(Collection<IEnemy> enemies);
}
