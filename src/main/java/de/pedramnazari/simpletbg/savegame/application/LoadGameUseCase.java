package de.pedramnazari.simpletbg.savegame.application;

import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

import java.util.Optional;

public interface LoadGameUseCase {
    Optional<GameSnapshot> loadMostRecentGame();
}
