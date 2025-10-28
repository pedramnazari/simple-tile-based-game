package de.pedramnazari.simpletbg.tilemap.config;

import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapJsonLoaderTest {

    @AfterEach
    void clearSystemProperty() {
        System.clearProperty(MapJsonLoader.EXTERNAL_DIRECTORY_PROPERTY);
    }

    @Test
    void load_includesMapsFromExternalDirectory() throws IOException {
        Path directory = Files.createTempDirectory("map-json-loader-test");
        Path file = directory.resolve("custom-map.json");
        Files.writeString(file, """
                {
                  "id": "custom-map",
                  "displayName": "Custom Map",
                  "heroStart": { "column": 0, "row": 0 },
                  "tiles": [[%d, %d], [%d, %d]],
                  "items": [[0, 0], [0, 0]],
                  "enemies": [[0, 0], [0, 0]]
                }
                """.formatted(TileType.FLOOR1.getType(), TileType.EXIT.getType(), TileType.FLOOR1.getType(), TileType.FLOOR1.getType()));

        System.setProperty(MapJsonLoader.EXTERNAL_DIRECTORY_PROPERTY, directory.toString());

        MapJsonLoader loader = new MapJsonLoader();
        List<GameMapDefinition> definitions = loader.load();

        boolean containsCustomMap = definitions.stream()
                .anyMatch(definition -> "custom-map".equals(definition.getId())
                        && "Custom Map".equals(definition.getDisplayName())
                        && definition.getHeroStartColumn() == 0
                        && definition.getHeroStartRow() == 0);

        assertTrue(containsCustomMap, "Loaded map definitions should include the custom map");
    }

    @Test
    void load_rejectsMatricesWithDifferentDimensions() throws IOException {
        Path directory = Files.createTempDirectory("map-json-loader-invalid");
        Path file = directory.resolve("invalid-map.json");
        Files.writeString(file, """
                {
                  "id": "invalid-map",
                  "displayName": "Invalid Map",
                  "heroStart": { "column": 0, "row": 0 },
                  "tiles": [[3, 3], [3, %d]],
                  "items": [[0, 0]],
                  "enemies": [[0, 0], [0, 0]]
                }
                """.formatted(TileType.EXIT.getType()));

        System.setProperty(MapJsonLoader.EXTERNAL_DIRECTORY_PROPERTY, directory.toString());

        MapJsonLoader loader = new MapJsonLoader();

        IllegalStateException exception = assertThrows(IllegalStateException.class, loader::load);
        assertTrue(exception.getMessage().contains("items matrix must have the same dimensions"));
    }
}
