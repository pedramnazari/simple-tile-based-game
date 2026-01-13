package de.pedramnazari.simpletbg.character.companion.service;

public interface ICompanionSubject {

    void registerObserver(ICompanionObserver observer);

    void removeObserver(ICompanionObserver observer);

    void notifyObservers();
}
