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
            ),
            new GameMapDefinition(
                    "arena",
                    "Arena",
                    ArenaMapConfig.MAP,
                    ArenaMapConfig.ITEMS,
                    ArenaMapConfig.ENEMIES,
                    ArenaMapConfig.HERO_START_COLUMN,
                    ArenaMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "forest",
                    "Forest",
                    ForestMapConfig.MAP,
                    ForestMapConfig.ITEMS,
                    ForestMapConfig.ENEMIES,
                    ForestMapConfig.HERO_START_COLUMN,
                    ForestMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "cavern",
                    "Cavern",
                    CavernMapConfig.MAP,
                    CavernMapConfig.ITEMS,
                    CavernMapConfig.ENEMIES,
                    CavernMapConfig.HERO_START_COLUMN,
                    CavernMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "island",
                    "Island",
                    IslandMapConfig.MAP,
                    IslandMapConfig.ITEMS,
                    IslandMapConfig.ENEMIES,
                    IslandMapConfig.HERO_START_COLUMN,
                    IslandMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "outpost",
                    "Outpost",
                    OutpostMapConfig.MAP,
                    OutpostMapConfig.ITEMS,
                    OutpostMapConfig.ENEMIES,
                    OutpostMapConfig.HERO_START_COLUMN,
                    OutpostMapConfig.HERO_START_ROW
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
