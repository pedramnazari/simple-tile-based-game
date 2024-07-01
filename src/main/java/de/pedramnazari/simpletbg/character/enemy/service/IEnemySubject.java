package de.pedramnazari.simpletbg.character.enemy.service;

public interface IEnemySubject {

        void registerObserver(IEnemyObserver observer);

        void removeObserver(IEnemyObserver observer);

        void notifyObservers();
}
