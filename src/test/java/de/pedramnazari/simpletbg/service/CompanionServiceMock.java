package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.tilemap.model.ICompanion;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.ICompanionService;

import java.util.Collection;
import java.util.List;

public class CompanionServiceMock implements ICompanionService {
    @Override
    public void init(ICompanion companion) {
    }

    @Override
    public void updateCompanions(GameContext gameContext) {
    }

    @Override
    public Collection<ICompanion> getCompanions() {
        return List.of();
    }

    @Override
    public boolean isInitialized() {
        return true;
    }
}
