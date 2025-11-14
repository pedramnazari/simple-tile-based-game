package de.pedramnazari.simpletbg.savegame.application;

import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;

public record LoadedGame(GameWorldController controller, GameMapDefinition mapDefinition) {
}
