package de.pedramnazari.simpletbg.savegame.application.service;

import de.pedramnazari.simpletbg.savegame.application.HasSavedGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;

import java.util.Objects;

public class HasSavedGameService implements HasSavedGameUseCase {

    private final GameSnapshotRepository repository;

    public HasSavedGameService(GameSnapshotRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    public boolean hasSave() {
        return repository.hasSnapshot();
    }
}
