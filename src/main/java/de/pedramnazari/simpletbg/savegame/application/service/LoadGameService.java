package de.pedramnazari.simpletbg.savegame.application.service;

import de.pedramnazari.simpletbg.savegame.application.LoadGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class LoadGameService implements LoadGameUseCase {

    private static final Logger logger = Logger.getLogger(LoadGameService.class.getName());

    private final GameSnapshotRepository repository;

    public LoadGameService(GameSnapshotRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    public Optional<GameSnapshot> loadMostRecentGame() {
        Optional<GameSnapshot> snapshot = repository.loadLatest();
        if (snapshot.isEmpty()) {
            logger.info("No saved game available");
        }
        return snapshot;
    }
}
