package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapDesignValidatorTest {

    private MapDesignValidator validator;

    @BeforeEach
    void setUp() {
        validator = MapDesignValidator.createDefault();
    }

    @Test
    void validate_allowsMapWithoutPortals() {
        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()}
                },
                emptyElements(),
                emptyElements()
        );

        assertDoesNotThrow(() -> validator.validate(context));
    }

    @Test
    void validate_rejectsInvalidPortalCount() {
        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.PORTAL.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(),
                emptyElements()
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsPortalSurroundedByDestructibleWalls() {
        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.EXIT.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.PORTAL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.FLOOR1.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType(), TileType.PORTAL.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(4),
                emptyElements(4)
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsMissingExit() {
        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()}
                },
                emptyElements(),
                emptyElements()
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsExitSurroundedByDestructibleWalls() {
        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.EXIT.getType(), TileType.DESTRUCTIBLE_WALL.getType()},
                        {TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType(), TileType.DESTRUCTIBLE_WALL.getType()}
                },
                emptyElements(3),
                emptyElements(3)
        );

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

        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.FLOOR1.getType()},
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()}
                },
                items,
                enemies
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsItemOnPortal() {
        int[][] items = new int[][]{
                {TileType.EMPTY.getType(), TileType.WEAPON_SWORD.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.PORTAL.getType()},
                        {TileType.FLOOR1.getType(), TileType.PORTAL.getType()}
                },
                items,
                emptyElements()
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsItemOnIndestructibleWall() {
        int[][] items = new int[][]{
                {TileType.EMPTY.getType(), TileType.WEAPON_SWORD.getType()},
                {TileType.EMPTY.getType(), TileType.EMPTY.getType()}
        };

        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.FLOOR1.getType(), TileType.WALL.getType()},
                        {TileType.FLOOR1.getType(), TileType.EXIT.getType()}
                },
                items,
                emptyElements()
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsHorizontalEnemyBlockedByWalls() {
        MapValidationContext context = new MapValidationContext(
                new int[][]{
                        {TileType.EXIT.getType(), TileType.WALL.getType(), TileType.FLOOR1.getType(), TileType.WALL.getType()}
                },
                new int[][]{{TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.EMPTY.getType()}},
                new int[][]{{TileType.EMPTY.getType(), TileType.EMPTY.getType(), TileType.ENEMY_LR.getType(), TileType.EMPTY.getType()}}
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(context));
    }

    @Test
    void validate_rejectsVerticalEnemyBlockedByWalls() {
        MapValidationContext context = new MapValidationContext(
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
                }
        );

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

        MapValidationContext context = new MapValidationContext(map, items, enemies);

        assertDoesNotThrow(() -> validator.validate(context));
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
