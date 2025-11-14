package de.pedramnazari.simpletbg.savegame.application.port;

import de.pedramnazari.simpletbg.savegame.application.LoadedGame;
import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

public interface ActiveGameStateRestorer {
    LoadedGame restore(GameSnapshot snapshot);
}
