package de.pedramnazari.simpletbg.tilemap.config;

import java.util.List;

public final class GameMaps {

    private static final List<GameMapDefinition> AVAILABLE_MAPS = List.of(
            new GameMapDefinition(
                    "scrolling-map",
                    "Scrolling Map",
                    ScrollingMapConfig.MAP,
                    ScrollingMapConfig.ITEMS,
                    ScrollingMapConfig.ENEMIES,
                    ScrollingMapConfig.HERO_START_COLUMN,
                    ScrollingMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "labyrinth",
                    "Labyrinth",
                    LabyrinthMapConfig.MAP,
                    LabyrinthMapConfig.ITEMS,
                    LabyrinthMapConfig.ENEMIES,
                    LabyrinthMapConfig.HERO_START_COLUMN,
                    LabyrinthMapConfig.HERO_START_ROW
            )
    );

    private GameMaps() {
    }

    public static List<GameMapDefinition> availableMaps() {
        return AVAILABLE_MAPS;
    }

    public static GameMapDefinition defaultMap() {
        return AVAILABLE_MAPS.get(0);
    }
}
