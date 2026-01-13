package de.pedramnazari.simpletbg.tilemap.config;

import java.util.List;

final class ProgrammaticGameMapSource implements GameMapSource {

    private static final List<GameMapDefinition> MAPS = List.of(
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
                    "elemental-arena",
                    "Elemental Arena",
                    ElementalArenaMapConfig.MAP,
                    ElementalArenaMapConfig.ITEMS,
                    ElementalArenaMapConfig.ENEMIES,
                    ElementalArenaMapConfig.HERO_START_COLUMN,
                    ElementalArenaMapConfig.HERO_START_ROW
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
            ),
            new GameMapDefinition(
                    "desert",
                    "Desert",
                    DesertMapConfig.MAP,
                    DesertMapConfig.ITEMS,
                    DesertMapConfig.ENEMIES,
                    DesertMapConfig.HERO_START_COLUMN,
                    DesertMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "glacier",
                    "Glacier",
                    GlacierMapConfig.MAP,
                    GlacierMapConfig.ITEMS,
                    GlacierMapConfig.ENEMIES,
                    GlacierMapConfig.HERO_START_COLUMN,
                    GlacierMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "citadel",
                    "Citadel",
                    CitadelMapConfig.MAP,
                    CitadelMapConfig.ITEMS,
                    CitadelMapConfig.ENEMIES,
                    CitadelMapConfig.HERO_START_COLUMN,
                    CitadelMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "swamp",
                    "Swamp",
                    SwampMapConfig.MAP,
                    SwampMapConfig.ITEMS,
                    SwampMapConfig.ENEMIES,
                    SwampMapConfig.HERO_START_COLUMN,
                    SwampMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "volcano",
                    "Volcano",
                    VolcanoMapConfig.MAP,
                    VolcanoMapConfig.ITEMS,
                    VolcanoMapConfig.ENEMIES,
                    VolcanoMapConfig.HERO_START_COLUMN,
                    VolcanoMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "meadow",
                    "Meadow",
                    MeadowMapConfig.MAP,
                    MeadowMapConfig.ITEMS,
                    MeadowMapConfig.ENEMIES,
                    MeadowMapConfig.HERO_START_COLUMN,
                    MeadowMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "temple",
                    "Temple",
                    TempleMapConfig.MAP,
                    TempleMapConfig.ITEMS,
                    TempleMapConfig.ENEMIES,
                    TempleMapConfig.HERO_START_COLUMN,
                    TempleMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "canyon",
                    "Canyon",
                    CanyonMapConfig.MAP,
                    CanyonMapConfig.ITEMS,
                    CanyonMapConfig.ENEMIES,
                    CanyonMapConfig.HERO_START_COLUMN,
                    CanyonMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "ruins",
                    "Ruins",
                    RuinsMapConfig.MAP,
                    RuinsMapConfig.ITEMS,
                    RuinsMapConfig.ENEMIES,
                    RuinsMapConfig.HERO_START_COLUMN,
                    RuinsMapConfig.HERO_START_ROW
            ),
            new GameMapDefinition(
                    "stronghold",
                    "Stronghold",
                    StrongholdMapConfig.MAP,
                    StrongholdMapConfig.ITEMS,
                    StrongholdMapConfig.ENEMIES,
                    StrongholdMapConfig.HERO_START_COLUMN,
                    StrongholdMapConfig.HERO_START_ROW
            )
    );

    @Override
    public List<GameMapDefinition> load() {
        return MAPS;
    }
}
