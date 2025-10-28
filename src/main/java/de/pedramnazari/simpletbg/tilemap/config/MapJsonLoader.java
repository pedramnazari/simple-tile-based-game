package de.pedramnazari.simpletbg.tilemap.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class MapJsonLoader implements GameMapSource {

    static final String DEFAULT_RESOURCE_DIRECTORY = "maps";
    static final String EXTERNAL_DIRECTORY_PROPERTY = "simpletbg.maps.dir";

    private final ObjectMapper objectMapper;
    private final ClassLoader classLoader;
    private final String resourceDirectory;
    private final String externalDirectoryProperty;

    MapJsonLoader() {
        this(createObjectMapper(), Thread.currentThread().getContextClassLoader(), DEFAULT_RESOURCE_DIRECTORY, EXTERNAL_DIRECTORY_PROPERTY);
    }

    MapJsonLoader(ObjectMapper objectMapper, ClassLoader classLoader, String resourceDirectory, String externalDirectoryProperty) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.classLoader = Objects.requireNonNullElse(classLoader, MapJsonLoader.class.getClassLoader());
        this.resourceDirectory = Objects.requireNonNull(resourceDirectory, "resourceDirectory must not be null");
        this.externalDirectoryProperty = Objects.requireNonNull(externalDirectoryProperty, "externalDirectoryProperty must not be null");
    }

    @Override
    public List<GameMapDefinition> load() {
        List<GameMapDefinition> definitions = new ArrayList<>();
        definitions.addAll(loadFromResources());
        definitions.addAll(loadFromExternalDirectories());
        return definitions;
    }

    private List<GameMapDefinition> loadFromResources() {
        List<GameMapDefinition> result = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources(resourceDirectory);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                result.addAll(readAll(url));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load map resources from classpath directory '" + resourceDirectory + "'", e);
        }
        return result;
    }

    private List<GameMapDefinition> readAll(URL url) {
        String protocol = url.getProtocol().toLowerCase(Locale.ROOT);
        if ("file".equals(protocol)) {
            try {
                return readAll(Paths.get(url.toURI()));
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Invalid URI for map resource: " + url, e);
            }
        }

        if ("jar".equals(protocol)) {
            try {
                URI uri = url.toURI();
                try (FileSystem fileSystem = newFileSystem(uri)) {
                    return readAll(fileSystem.getPath(resourceDirectory));
                } catch (FileSystemAlreadyExistsException ex) {
                    FileSystem fileSystem = FileSystems.getFileSystem(uri);
                    return readAll(fileSystem.getPath(resourceDirectory));
                }
            } catch (URISyntaxException | IOException e) {
                throw new IllegalStateException("Failed to read map resources from " + url, e);
            }
        }

        throw new IllegalStateException("Unsupported protocol '" + protocol + "' for map resource " + url);
    }

    private List<GameMapDefinition> loadFromExternalDirectories() {
        String configuredDirectories = System.getProperty(externalDirectoryProperty);
        if (configuredDirectories == null || configuredDirectories.isBlank()) {
            return List.of();
        }

        String[] directories = configuredDirectories.split(java.io.File.pathSeparator);
        List<GameMapDefinition> result = new ArrayList<>();
        for (String directory : directories) {
            if (directory == null || directory.isBlank()) {
                continue;
            }
            Path path = Paths.get(directory.trim());
            result.addAll(readAll(path));
        }
        return result;
    }

    private List<GameMapDefinition> readAll(Path root) {
        if (!Files.exists(root)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json"))
                    .sorted(Comparator.comparing(Path::toString))
                    .map(this::readDefinition)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read map definitions from " + root, e);
        }
    }

    private GameMapDefinition readDefinition(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            JsonGameMapDefinition definition = objectMapper.readValue(inputStream, JsonGameMapDefinition.class);
            return toGameMapDefinition(definition, path.toString());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read map definition from " + path, e);
        }
    }

    private GameMapDefinition toGameMapDefinition(JsonGameMapDefinition json, String source) {
        String id = requireText(json.id(), "id", source);
        String displayName = requireText(json.displayName(), "displayName", source);
        JsonHeroStart heroStart = Objects.requireNonNull(json.heroStart(), "heroStart must not be null in " + source);

        int[][] tiles = copyMatrix(requireMatrix(json.tiles(), "tiles", source));
        validateTileValues(tiles, "tiles", source);

        int height = tiles.length;
        int width = tiles[0].length;

        int[][] items = json.items() == null
                ? createEmptyMatrix(height, width)
                : copyAndValidateOptionalMatrix(json.items(), height, width, "items", source);
        validateTileValues(items, "items", source);

        int[][] enemies = json.enemies() == null
                ? createEmptyMatrix(height, width)
                : copyAndValidateOptionalMatrix(json.enemies(), height, width, "enemies", source);
        validateTileValues(enemies, "enemies", source);

        ensureHeroWithinBounds(heroStart, height, width, source);

        return new GameMapDefinition(
                id,
                displayName,
                tiles,
                items,
                enemies,
                heroStart.column(),
                heroStart.row()
        );
    }

    private static void ensureHeroWithinBounds(JsonHeroStart heroStart, int height, int width, String source) {
        if (heroStart.row() < 0 || heroStart.column() < 0 || heroStart.row() >= height || heroStart.column() >= width) {
            throw new IllegalStateException("Hero start position (" + heroStart.column() + "," + heroStart.row()
                    + ") is outside of the map bounds in " + source);
        }
    }

    private static void validateTileValues(int[][] matrix, String name, String source) {
        for (int[] row : matrix) {
            for (int value : row) {
                try {
                    TileType.fromType(value);
                } catch (IllegalArgumentException ex) {
                    throw new IllegalStateException("Unknown tile type " + value + " in " + name + " matrix for " + source, ex);
                }
            }
        }
    }

    private static int[][] copyAndValidateOptionalMatrix(int[][] matrix, int expectedHeight, int expectedWidth, String name, String source) {
        int[][] validated = requireMatrix(matrix, name, source);
        if (validated.length != expectedHeight || validated[0].length != expectedWidth) {
            throw new IllegalStateException(name + " matrix must have the same dimensions as the tiles matrix in " + source);
        }
        return copyMatrix(validated);
    }

    private static int[][] requireMatrix(int[][] matrix, String name, String source) {
        if (matrix == null) {
            throw new IllegalStateException(name + " matrix must not be null in " + source);
        }
        if (matrix.length == 0) {
            throw new IllegalStateException(name + " matrix must not be empty in " + source);
        }

        int expectedWidth = matrix[0].length;
        if (expectedWidth == 0) {
            throw new IllegalStateException(name + " matrix must not contain empty rows in " + source);
        }

        for (int[] row : matrix) {
            if (row == null || row.length != expectedWidth) {
                throw new IllegalStateException(name + " matrix must be rectangular in " + source);
            }
        }

        return matrix;
    }

    private static int[][] copyMatrix(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix[0].length];
        for (int row = 0; row < matrix.length; row++) {
            System.arraycopy(matrix[row], 0, copy[row], 0, matrix[row].length);
        }
        return copy;
    }

    private static int[][] createEmptyMatrix(int height, int width) {
        int[][] matrix = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                matrix[row][column] = TileType.EMPTY.getType();
            }
        }
        return matrix;
    }

    private static String requireText(String value, String name, String source) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " must not be blank in " + source);
        }
        return value;
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return mapper;
    }

    private static FileSystem newFileSystem(URI uri) throws IOException {
        return FileSystems.newFileSystem(uri, java.util.Collections.emptyMap());
    }

    private record JsonGameMapDefinition(String id,
                                         String displayName,
                                         JsonHeroStart heroStart,
                                         int[][] tiles,
                                         int[][] items,
                                         int[][] enemies) {
    }

    private record JsonHeroStart(int column, int row) {
    }
}
