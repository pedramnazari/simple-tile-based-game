package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MapValidationContext {

    private final Tile[][] map;
    private final List<IItem> items;
    private final List<IEnemy> enemies;
    private final int[][] itemTypes;
    private final int[][] enemyTypes;

    public MapValidationContext(Tile[][] map, Collection<IItem> items, Collection<IEnemy> enemies) {
        this.map = requireMatrix(map, "map");
        this.items = List.copyOf(requireCollection(items, "items"));
        this.enemies = List.copyOf(requireCollection(enemies, "enemies"));
        this.itemTypes = createElementMatrix(getHeight(), getWidth(), this.items);
        this.enemyTypes = createElementMatrix(getHeight(), getWidth(), this.enemies);
    }

    public static MapValidationContext fromConfiguration(int[][] map, int[][] items, int[][] enemies) {
        final int[][] validatedMap = requireMatrix(map, "map");
        final int[][] validatedItems = requireMatrix(items, "items");
        final int[][] validatedEnemies = requireMatrix(enemies, "enemies");

        ensureSameDimensions(validatedMap, validatedItems, "items");
        ensureSameDimensions(validatedMap, validatedEnemies, "enemies");

        return new MapValidationContext(
                createTiles(validatedMap),
                createItems(validatedItems),
                createEnemies(validatedEnemies)
        );
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
        return map[row][column].getType();
    }

    public int getItemType(int row, int column) {
        return itemTypes[row][column];
    }

    public int getEnemyType(int row, int column) {
        return enemyTypes[row][column];
    }

    public List<MapPosition> findMapTiles(int tileType) {
        final List<MapPosition> positions = new ArrayList<>();

        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (map[row][col].getType() == tileType) {
                    positions.add(new MapPosition(row, col));
                }
            }
        }

        return positions;
    }

    private static Tile[][] createTiles(int[][] map) {
        final int height = map.length;
        final int width = map[0].length;

        Tile[][] tiles = new Tile[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                tiles[row][col] = new Tile(map[row][col], col, row);
            }
        }

        return tiles;
    }

    private static Collection<IItem> createItems(int[][] items) {
        final int height = items.length;
        final int width = items[0].length;
        List<IItem> result = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int type = items[row][col];
                if (!TileTypeClassifier.isEmpty(type)) {
                    result.add(new ConfiguredItem(type, col, row));
                }
            }
        }

        return result;
    }

    private static Collection<IEnemy> createEnemies(int[][] enemies) {
        final int height = enemies.length;
        final int width = enemies[0].length;
        List<IEnemy> result = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int type = enemies[row][col];
                if (!TileTypeClassifier.isEmpty(type)) {
                    result.add(new ConfiguredEnemy(type, col, row));
                }
            }
        }

        return result;
    }

    private static Tile[][] requireMatrix(Tile[][] matrix, String name) {
        Objects.requireNonNull(matrix, name + " must not be null");
        if (matrix.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty");
        }

        final int expectedWidth = matrix[0].length;
        if (expectedWidth == 0) {
            throw new IllegalArgumentException(name + " must not contain empty rows");
        }

        for (Tile[] row : matrix) {
            if (row == null || row.length != expectedWidth) {
                throw new IllegalArgumentException(name + " must be rectangular");
            }
            for (Tile tile : row) {
                Objects.requireNonNull(tile, name + " must not contain null tiles");
            }
        }

        return matrix;
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

    private static <T> Collection<T> requireCollection(Collection<T> collection, String name) {
        Objects.requireNonNull(collection, name + " must not be null");
        for (T element : collection) {
            Objects.requireNonNull(element, name + " must not contain null elements");
        }
        return collection;
    }

    private static <T extends ITileMapElement> int[][] createElementMatrix(int height, int width, Collection<T> elements) {
        int[][] matrix = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                matrix[row][col] = TileType.EMPTY.getType();
            }
        }

        for (T element : elements) {
            int row = element.getY();
            int col = element.getX();
            if (row < 0 || row >= height || col < 0 || col >= width) {
                throw new IllegalArgumentException("Element at (" + col + "," + row + ") is outside of the map bounds");
            }
            matrix[row][col] = element.getType();
        }

        return matrix;
    }

    public record MapPosition(int row, int column) {
    }

    private static final class ConfiguredItem implements IItem {
        private final int type;
        private final int x;
        private final int y;

        private ConfiguredItem(int type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        @Override
        public String getName() {
            return "Configured item";
        }

        @Override
        public String getDescription() {
            return "Item configured for validation";
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getType() {
            return type;
        }
    }

    private static final class ConfiguredEnemy implements IEnemy {
        private final int type;
        private int x;
        private int y;
        private MoveDirection moveDirection;
        private IMovementStrategy movementStrategy;
        private int health = 1;
        private int attackingPower;

        private ConfiguredEnemy(int type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public void setX(int x) {
            this.x = x;
        }

        @Override
        public void setY(int y) {
            this.y = y;
        }

        @Override
        public void setMoveDirection(MoveDirection moveDirection) {
            this.moveDirection = moveDirection;
        }

        @Override
        public Optional<MoveDirection> getMoveDirection() {
            return Optional.ofNullable(moveDirection);
        }

        @Override
        public void setMovementStrategy(IMovementStrategy movementStrategy) {
            this.movementStrategy = movementStrategy;
        }

        @Override
        public IMovementStrategy getMovementStrategy() {
            return movementStrategy;
        }

        @Override
        public int getHealth() {
            return health;
        }

        @Override
        public int increaseHealth(int health) {
            this.health += health;
            return this.health;
        }

        @Override
        public int decreaseHealth(int health) {
            this.health -= health;
            return this.health;
        }

        @Override
        public void setAttackingPower(int attackingPower) {
            this.attackingPower = attackingPower;
        }

        @Override
        public int getAttackingPower() {
            return attackingPower;
        }
    }
}
