package de.pedramnazari.simpletbg.drivers;

import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.quest.service.QuestService;
import de.pedramnazari.simpletbg.savegame.adapters.game.GameWorldStateReaderAdapter;
import de.pedramnazari.simpletbg.savegame.adapters.game.GameWorldStateRestorerAdapter;
import de.pedramnazari.simpletbg.savegame.adapters.persistence.json.JsonGameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.application.HasSavedGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.LoadGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.SaveGameUseCase;
import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateReader;
import de.pedramnazari.simpletbg.savegame.application.port.ActiveGameStateRestorer;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.application.service.HasSavedGameService;
import de.pedramnazari.simpletbg.savegame.application.service.LoadGameService;
import de.pedramnazari.simpletbg.savegame.application.service.SaveGameService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class GameApplication {

    private static final Path DEFAULT_SAVE_PATH = Paths.get(System.getProperty("user.home"), ".simpletbg", "saves", "latest.json");

    private static final GameSnapshotRepository REPOSITORY = new JsonGameSnapshotRepository(DEFAULT_SAVE_PATH);
    private static final ActiveGameStateRestorer RESTORER = new GameWorldStateRestorerAdapter();
    private static final LoadGameUseCase LOAD_GAME_USE_CASE = new LoadGameService(REPOSITORY, RESTORER);
    private static final HasSavedGameUseCase HAS_SAVED_GAME_USE_CASE = new HasSavedGameService(REPOSITORY);

    private static SaveGameUseCase saveGameUseCase;

    private GameApplication() {
    }

    public static synchronized void wireRuntime(GameWorldService gameWorldService, QuestService questService) {
        Objects.requireNonNull(gameWorldService, "gameWorldService");
        Objects.requireNonNull(questService, "questService");
        ActiveGameStateReader reader = new GameWorldStateReaderAdapter(gameWorldService, questService);
        saveGameUseCase = new SaveGameService(reader, REPOSITORY);
    }

    public static SaveGameUseCase getSaveGameUseCase() {
        if (saveGameUseCase == null) {
            throw new IllegalStateException("Game runtime not initialized yet");
        }
        return saveGameUseCase;
    }

    public static LoadGameUseCase getLoadGameUseCase() {
        return LOAD_GAME_USE_CASE;
    }

    public static HasSavedGameUseCase getHasSavedGameUseCase() {
        return HAS_SAVED_GAME_USE_CASE;
    }
}
