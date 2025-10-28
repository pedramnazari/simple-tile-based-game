package de.pedramnazari.simpletbg.tilemap.config;

import java.util.List;

interface GameMapSource {

    List<GameMapDefinition> load();
}
