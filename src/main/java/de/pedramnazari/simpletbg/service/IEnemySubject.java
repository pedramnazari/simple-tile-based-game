package de.pedramnazari.simpletbg.service;

public interface IEnemySubject {

        void registerObserver(IEnemyObserver observer);

        void removeObserver(IEnemyObserver observer);

        void notifyObservers();
}
