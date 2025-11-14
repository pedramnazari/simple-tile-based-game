package de.pedramnazari.simpletbg.savegame.domain;

import java.time.Instant;
import java.util.Objects;

public record SnapshotMetadata(int schemaVersion, Instant savedAt) {

    public SnapshotMetadata {
        Objects.requireNonNull(savedAt, "savedAt");
        if (schemaVersion < 0) {
            throw new IllegalArgumentException("schemaVersion must be positive");
        }
    }
}
