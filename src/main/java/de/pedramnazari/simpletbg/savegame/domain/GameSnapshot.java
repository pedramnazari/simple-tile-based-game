package de.pedramnazari.simpletbg.savegame.domain;

import java.util.List;
import java.util.Objects;

public record GameSnapshot(
        SnapshotMetadata metadata,
        String mapId,
        String currentMapIndex,
        HeroState hero,
        InventoryState inventory,
        List<WorldItemState> worldItems,
        List<EnemyState> enemies,
        QuestState quest,
        int score,
        boolean gameOver
) {

    public GameSnapshot {
        Objects.requireNonNull(metadata, "metadata");
        Objects.requireNonNull(mapId, "mapId");
        Objects.requireNonNull(currentMapIndex, "currentMapIndex");
        Objects.requireNonNull(hero, "hero");
        Objects.requireNonNull(inventory, "inventory");
        Objects.requireNonNull(worldItems, "worldItems");
        Objects.requireNonNull(enemies, "enemies");
        Objects.requireNonNull(quest, "quest");
    }
}
