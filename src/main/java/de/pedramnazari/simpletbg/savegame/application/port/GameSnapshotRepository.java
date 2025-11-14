package de.pedramnazari.simpletbg.savegame.application.port;

import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

import java.util.Optional;

public interface GameSnapshotRepository {
    void save(GameSnapshot snapshot);

    Optional<GameSnapshot> loadLatest();

    boolean hasSnapshot();
}
