package de.pedramnazari.simpletbg.tilemap.config;

import java.util.List;

public final class GameMaps {

    private static final GameMapRepository REPOSITORY = GameMapRepository.createDefault();

    private GameMaps() {
    }

    public static List<GameMapDefinition> availableMaps() {
        return REPOSITORY.getAll();
    }

    public static GameMapDefinition defaultMap() {
        return REPOSITORY.getDefaultMap();
    }

    public static void reload() {
        REPOSITORY.reload();
    }
}
