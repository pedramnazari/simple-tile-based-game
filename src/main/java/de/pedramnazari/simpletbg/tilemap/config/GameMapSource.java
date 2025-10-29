package de.pedramnazari.simpletbg.tilemap.config;

import java.util.List;

interface GameMapSource {

    /**
     * Loads all map definitions exposed by the source so that
     * {@link GameMapRepository} can aggregate them with definitions
     * from other providers (for example JSON files or programmatic maps).
     *
     * @return the map definitions supplied by this source
     */
    List<GameMapDefinition> load();
}
