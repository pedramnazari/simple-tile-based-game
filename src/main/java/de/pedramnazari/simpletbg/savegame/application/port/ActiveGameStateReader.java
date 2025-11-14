package de.pedramnazari.simpletbg.savegame.application.port;

import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

public interface ActiveGameStateReader {
    GameSnapshot capture();
}
