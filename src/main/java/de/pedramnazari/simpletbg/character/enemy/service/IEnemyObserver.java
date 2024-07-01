package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;

import java.util.Collection;

public interface IEnemyObserver {

    void update(Collection<Enemy> enemies);
}
