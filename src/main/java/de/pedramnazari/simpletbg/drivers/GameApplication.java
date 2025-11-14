package de.pedramnazari.simpletbg.drivers;

import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.quest.service.QuestService;
import de.pedramnazari.simpletbg.savegame.adapters.game.GameSessionRestorer;
import de.pedramnazari.simpletbg.savegame.adapters.game.GameWorldStateReaderAdapter;
import de.pedramnazari.simpletbg.savegame.adapters.persistence.json.JsonGameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.application.HasSavedGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.LoadGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.SaveGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateReader;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.application.service.HasSavedGameService;
import de.pedramnazari.simpletbg.savegame.application.service.LoadGameService;
import de.pedramnazari.simpletbg.savegame.application.service.SaveGameService;
import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public final class GameApplication {

    private static final Path DEFAULT_SAVE_PATH = Paths.get(System.getProperty("user.home"), ".simpletbg", "saves", "latest.json");

    private final GameSnapshotRepository repository;
    private final LoadGameUseCase loadGameUseCase;
    private final HasSavedGameUseCase hasSavedGameUseCase;
    private final GameSessionRestorer sessionRestorer;

    public GameApplication() {
        this(DEFAULT_SAVE_PATH);
    }

    public GameApplication(Path savePath) {
        this.repository = new JsonGameSnapshotRepository(Objects.requireNonNull(savePath, "savePath"));
        this.loadGameUseCase = new LoadGameService(repository);
        this.hasSavedGameUseCase = new HasSavedGameService(repository);
        this.sessionRestorer = new GameSessionRestorer();
    }

    public GameSession startNewSession(GameMapDefinition mapDefinition) {
        GameContext.resetInstance();
        GameInitializer.GameRuntime runtime = GameInitializer.initAndStartGame(Objects.requireNonNull(mapDefinition, "mapDefinition"));
        return createSession(runtime);
    }

    public Optional<GameSession> loadMostRecentSession() {
        Optional<GameSnapshot> snapshot = loadGameUseCase.loadMostRecentGame();
        if (snapshot.isEmpty()) {
            return Optional.empty();
        }
        GameInitializer.GameRuntime runtime = sessionRestorer.restore(snapshot.get());
        return Optional.of(createSession(runtime));
    }

    public boolean hasSavedGame() {
        return hasSavedGameUseCase.hasSave();
    }

    private GameSession createSession(GameInitializer.GameRuntime runtime) {
        SaveGameUseCase saveGameUseCase = createSaveUseCase(runtime.gameWorldService(), runtime.questService());
        return new DefaultGameSession(runtime.controller(), saveGameUseCase, runtime.mapDefinition());
    }

    private SaveGameUseCase createSaveUseCase(GameWorldService gameWorldService, QuestService questService) {
        ActiveGameStateReader reader = new GameWorldStateReaderAdapter(gameWorldService, questService);
        return new SaveGameService(reader, repository);
    }
}
