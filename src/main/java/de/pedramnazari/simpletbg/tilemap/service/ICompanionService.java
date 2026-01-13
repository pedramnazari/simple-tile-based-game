package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.ICompanion;

import java.util.Collection;

public interface ICompanionService {
    void init(ICompanion companion);
    
    void updateCompanions(GameContext gameContext);
    
    Collection<ICompanion> getCompanions();
    
    boolean isInitialized();
}
