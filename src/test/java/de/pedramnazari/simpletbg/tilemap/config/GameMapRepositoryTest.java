package de.pedramnazari.simpletbg.tilemap.config;

import de.pedramnazari.simpletbg.tilemap.config.validation.MapDesignValidator;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameMapRepositoryTest {

    @Test
    void reload_throwsWhenDuplicateIdsDetected() {
        GameMapDefinition map = createMap("duplicate");

        assertThrows(IllegalStateException.class, () -> new GameMapRepository(
                List.of(() -> List.of(map), () -> List.of(map)),
                MapDesignValidator.createDefault()
        ));
    }

    @Test
    void reload_validatesHeroPlacement() {
        GameMapDefinition invalidHeroPosition = new GameMapDefinition(
                "invalid-hero",
                "Invalid Hero",
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                new int[][]{
                        {0, 0},
                        {0, 0}
                },
                new int[][]{
                        {0, 0},
                        {0, 0}
                },
                0,
                5
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new GameMapRepository(
                List.of(() -> List.of(invalidHeroPosition)),
                MapDesignValidator.createDefault()
        ));
        assertEquals("Hero start position (0,5) is outside of map bounds for map 'invalid-hero'", exception.getMessage());
    }

    @Test
    void getDefaultMap_returnsFirstAggregatedMap() {
        GameMapDefinition first = createMap("first");
        GameMapDefinition second = createMap("second");

        GameMapRepository repository = new GameMapRepository(
                List.of(() -> List.of(first, second)),
                MapDesignValidator.createDefault()
        );

        assertEquals(first, repository.getDefaultMap());
    }

    private static GameMapDefinition createMap(String id) {
        return new GameMapDefinition(
                id,
                id,
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                new int[][]{
                        {0, 0},
                        {0, 0}
                },
                new int[][]{
                        {0, 0},
                        {0, 0}
                },
                0,
                0
        );
    }
}
