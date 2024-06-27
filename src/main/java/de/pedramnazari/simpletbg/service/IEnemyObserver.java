package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;

import java.util.Collection;

public interface IEnemyObserver {

    void update(Collection<Enemy> enemies);
}
