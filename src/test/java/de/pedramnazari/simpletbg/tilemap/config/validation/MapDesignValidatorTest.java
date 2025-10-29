package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapDesignValidatorTest {

    private MapDesignValidator validator;

    @BeforeEach
    void setUp() {
        validator = MapDesignValidator.createDefault();
    }

    @Test
    void validate_acceptsAllAvailableGameMaps() {
        for (GameMapDefinition definition : GameMaps.availableMaps()) {
            MapValidationContext context = MapValidationContext.fromConfiguration(
                    definition.getMap(),
                    definition.getItems(),
                    definition.getEnemies(),
                    definition.getHeroStartRow(),
                    definition.getHeroStartColumn()
            );

            assertDoesNotThrow(() -> validator.validate(context),
                    () -> "Map " + definition.getId() + " should be valid");
        }
    }

    @Test
    void validate_allowsMapWithoutPortals() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()}
                },
                emptyElements(),
                emptyElements(),
                0,
                0
        );

        assertDoesNotThrow(() -> validator.validate(context));
    }

    @Test
    void validate_rejectsInvalidPortalCount() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.PORTAL.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(),
                emptyElements(),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new PortalConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsPortalSurroundedByDestructibleWalls() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.EXIT.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.PORTAL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.FLOOR1.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType(), TileType.PORTAL.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(4),
                emptyElements(4),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new PortalConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsMissingExit() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(),
                emptyElements(),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new ExitConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsHeroOutsideBounds() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                new int[][]{{TileType.EMPTY.getType(), TileType.EMPTY.getType()}},
                new int[][]{{TileType.EMPTY.getType(), TileType.EMPTY.getType()}},
                5,
                0
        );

        MapDesignValidator validator = createValidator(new HeroStartConstraint());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validator.validate(context));
        assertTrue(exception.getMessage().contains("outside of map bounds"));
    }

    @Test
    void validate_rejectsHeroOnNonWalkableTile() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.WALL.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(),
                emptyElements(),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new HeroStartConstraint());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validator.validate(context));
        assertTrue(exception.getMessage().contains("must be walkable"));
    }

    @Test
    void validate_rejectsExitSurroundedByDestructibleWalls() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.EXIT.getType(), TileType.DESTRUCTIBLE_WALL.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType()}
                },
                emptyElements(3),
                emptyElements(3),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new ExitConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsItemAndEnemyOverlap() {
        int[][] items = new int[][]{
                {TileType.EMPTY.getType(), TileType.WEAPON_SWORD.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };
        int[][] enemies = new int[][]{
                {TileType.EMPTY.getType(), TileType.ENEMY_LR.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()}
                },
                items,
                enemies,
                0,
                0
        );

        MapDesignValidator validator = createValidator(new ElementOverlapConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsItemOnPortal() {
        int[][] items = new int[][]{
                {TileType.EMPTY.getType(), TileType.WEAPON_SWORD.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.PORTAL.getType()},
                        {TileType.FLOOR1.getType(), TileType.PORTAL.getType()}
                },
                items,
                emptyElements(),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new ElementOverlapConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsItemOnIndestructibleWall() {
        int[][] items = new int[][]{
                {TileType.EMPTY.getType(), TileType.WEAPON_SWORD.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.WALL.getType()},
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()}
                },
                items,
                emptyElements(),
                0,
                0
        );

        MapDesignValidator validator = createValidator(new ElementOverlapConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsHorizontalEnemyBlockedByWalls() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.EXIT.getType(), TileType.WALL.getType(), TileType.FLOOR1.getType(), TileType.WALL.getType()}
                },
                new int[][]{{TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()}},
                new int[][]{{TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.ENEMY_LR.getType(), TileType.EMPTY.getType()}},
                0,
                0
        );

        MapDesignValidator validator = createValidator(new HorizontalEnemyMovementConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsVerticalEnemyBlockedByWalls() {
        MapValidationContext context = MapValidationContext.fromConfiguration(
                new int[][]{
                        {TileType.EXIT.getType()},
                        {TileType.WALL.getType()},
                        {TileType.FLOOR1.getType()},
                        {TileType.WALL.getType()}
                },
                new int[][]{
                        {TileType.EMPTY.getType()},
                        {TileType.EMPTY.getType()},
                        {TileType.EMPTY.getType()},
                        {TileType.EMPTY.getType()}
                },
                new int[][]{
                        {TileType.EMPTY.getType()},
                        {TileType.EMPTY.getType()},
                        {TileType.ENEMY_TD.getType()},
                        {TileType.EMPTY.getType()}
                },
                0,
                0
        );

        MapDesignValidator validator = createValidator(new VerticalEnemyMovementConstraint());

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_acceptsValidConfiguration() {
        int[][] map = new int[][]{
                {TileType.FLOOR1.getType(), TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                {TileType.PORTAL.getType(), TileType.FLOOR1.getType(), TileType.PORTAL.getType()},
                {TileType.FLOOR1.getType(), TileType.EXIT.getType(), TileType.FLOOR1.getType()}
        };

        int[][] items = new int[][]{
                {TileType.EMPTY.getType(), TileType.WEAPON_SWORD.getType(), TileType.EMPTY.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        int[][] enemies = new int[][]{
                {TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        MapValidationContext context = MapValidationContext.fromConfiguration(map, items, enemies, 1, 1);

        assertDoesNotThrow(() -> validator.validate(context));
    }

    private MapDesignValidator createValidator(MapConstraint... constraints) {
        return new MapDesignValidator(List.of(constraints));
    }

    private static int[][] emptyElements() {
        return emptyElements(2);
    }

    private static int[][] emptyElements(int size) {
        int[][] empty = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                empty[row][col] = TileType.EMPTY.getType();
            }
        }
        return empty;
    }
}
