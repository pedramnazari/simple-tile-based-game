package de.pedramnazari.simpletbg.drivers;

import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.savegame.application.SaveGameUseCase;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;

public interface GameSession {
    GameWorldController controller();
    SaveGameUseCase saveGame();
    GameMapDefinition mapDefinition();
}
