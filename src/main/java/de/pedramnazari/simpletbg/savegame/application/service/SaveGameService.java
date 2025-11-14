package de.pedramnazari.simpletbg.savegame.application.service;

import de.pedramnazari.simpletbg.savegame.application.SaveGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateReader;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

import java.util.Objects;
import java.util.logging.Logger;

public class SaveGameService implements SaveGameUseCase {

    private static final Logger logger = Logger.getLogger(SaveGameService.class.getName());

    private final ActiveGameStateReader stateReader;
    private final GameSnapshotRepository repository;

    public SaveGameService(ActiveGameStateReader stateReader, GameSnapshotRepository repository) {
        this.stateReader = Objects.requireNonNull(stateReader, "stateReader");
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    public void saveCurrentGame() {
        GameSnapshot snapshot = stateReader.capture();
        repository.save(snapshot);
        logger.info("Game snapshot persisted");
    }
}
