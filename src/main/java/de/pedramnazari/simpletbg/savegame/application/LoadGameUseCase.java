package de.pedramnazari.simpletbg.savegame.application;

import java.util.Optional;

public interface LoadGameUseCase {
    Optional<LoadedGame> loadMostRecentGame();
}
