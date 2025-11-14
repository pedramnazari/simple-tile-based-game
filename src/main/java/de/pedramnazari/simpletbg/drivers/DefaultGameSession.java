package de.pedramnazari.simpletbg.drivers;

import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.savegame.application.SaveGameUseCase;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;

import java.util.Objects;

public final class DefaultGameSession implements GameSession {

    private final GameWorldController controller;
    private final SaveGameUseCase saveGameUseCase;
    private final GameMapDefinition mapDefinition;

    public DefaultGameSession(GameWorldController controller,
                              SaveGameUseCase saveGameUseCase,
                              GameMapDefinition mapDefinition) {
        this.controller = Objects.requireNonNull(controller, "controller");
        this.saveGameUseCase = Objects.requireNonNull(saveGameUseCase, "saveGameUseCase");
        this.mapDefinition = Objects.requireNonNull(mapDefinition, "mapDefinition");
    }

    @Override
    public GameWorldController controller() {
        return controller;
    }

    @Override
    public SaveGameUseCase saveGame() {
        return saveGameUseCase;
    }

    @Override
    public GameMapDefinition mapDefinition() {
        return mapDefinition;
    }
}
