package de.pedramnazari.simpletbg.character.companion.service;

import de.pedramnazari.simpletbg.tilemap.model.ICompanion;

import java.util.Collection;

public interface ICompanionObserver {

    void updateCompanions(Collection<ICompanion> companions);
}
