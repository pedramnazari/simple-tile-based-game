package de.pedramnazari.simpletbg.savegame.application.service;

import de.pedramnazari.simpletbg.savegame.application.LoadGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.LoadedGame;
import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateRestorer;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class LoadGameService implements LoadGameUseCase {

    private static final Logger logger = Logger.getLogger(LoadGameService.class.getName());

    private final GameSnapshotRepository repository;
    private final ActiveGameStateRestorer restorer;

    public LoadGameService(GameSnapshotRepository repository, ActiveGameStateRestorer restorer) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.restorer = Objects.requireNonNull(restorer, "restorer");
    }

    @Override
    public Optional<LoadedGame> loadMostRecentGame() {
        Optional<GameSnapshot> snapshot = repository.loadLatest();
        if (snapshot.isEmpty()) {
            logger.info("No saved game available");
            return Optional.empty();
        }
        return snapshot.map(restorer::restore);
    }
}
