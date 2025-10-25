package de.pedramnazari.simpletbg.tilemap.config.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapValidationContext {

    private final int[][] map;
    private final int[][] items;
    private final int[][] enemies;

    public MapValidationContext(int[][] map, int[][] items, int[][] enemies) {
        this.map = requireMatrix(map, "map");
        this.items = requireMatrix(items, "items");
        this.enemies = requireMatrix(enemies, "enemies");

        ensureSameDimensions(this.map, this.items, "items");
        ensureSameDimensions(this.map, this.enemies, "enemies");
    }

    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }

    public boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < getHeight() && column >= 0 && column < getWidth();
    }

    public int getMapTileType(int row, int column) {
        return map[row][column];
    }

    public int getItemType(int row, int column) {
        return items[row][column];
    }

    public int getEnemyType(int row, int column) {
        return enemies[row][column];
    }

    public List<MapPosition> findMapTiles(int tileType) {
        final List<MapPosition> positions = new ArrayList<>();

        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (map[row][col] == tileType) {
                    positions.add(new MapPosition(row, col));
                }
            }
        }

        return positions;
    }

    private static int[][] requireMatrix(int[][] matrix, String name) {
        Objects.requireNonNull(matrix, name + " must not be null");
        if (matrix.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty");
        }

        final int expectedWidth = matrix[0].length;
        if (expectedWidth == 0) {
            throw new IllegalArgumentException(name + " must not contain empty rows");
        }

        for (int[] row : matrix) {
            if (row == null || row.length != expectedWidth) {
                throw new IllegalArgumentException(name + " must be rectangular");
            }
        }

        return matrix;
    }

    private static void ensureSameDimensions(int[][] reference, int[][] matrix, String name) {
        if (reference.length != matrix.length || reference[0].length != matrix[0].length) {
            throw new IllegalArgumentException(name + " matrix must have the same dimensions as the map");
        }
    }

    public record MapPosition(int row, int column) {
    }
}
