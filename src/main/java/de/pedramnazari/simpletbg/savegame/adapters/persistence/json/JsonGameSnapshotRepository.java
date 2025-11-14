package de.pedramnazari.simpletbg.savegame.adapters.persistence.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.pedramnazari.simpletbg.savegame.application.port.GameSnapshotRepository;
import de.pedramnazari.simpletbg.savegame.domain.GameSnapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonGameSnapshotRepository implements GameSnapshotRepository {

    private static final Logger logger = Logger.getLogger(JsonGameSnapshotRepository.class.getName());

    private final Path snapshotPath;
    private final ObjectMapper objectMapper;

    public JsonGameSnapshotRepository(Path snapshotPath) {
        this.snapshotPath = snapshotPath;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public void save(GameSnapshot snapshot) {
        try {
            Files.createDirectories(snapshotPath.getParent());
            Path tempFile = Files.createTempFile(snapshotPath.getParent(), "snapshot", ".tmp");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile.toFile(), snapshot);
            Files.move(tempFile, snapshotPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to persist game snapshot", e);
            throw new IllegalStateException("Could not save game snapshot", e);
        }
    }

    @Override
    public Optional<GameSnapshot> loadLatest() {
        if (!Files.exists(snapshotPath)) {
            return Optional.empty();
        }
        try {
            GameSnapshot snapshot = objectMapper.readValue(snapshotPath.toFile(), GameSnapshot.class);
            return Optional.of(snapshot);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load game snapshot", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean hasSnapshot() {
        return Files.exists(snapshotPath);
    }
}
