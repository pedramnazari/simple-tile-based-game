package de.pedramnazari.simpletbg.drivers;

import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for GameInitializer that validates the game can be started
 * with various map definitions without errors or exceptions.
 */
public class GameInitializerTest {

    private TestLogHandler testLogHandler;
    private Logger rootLogger;

    @BeforeEach
    public void setUp() {
        // Reset GameContext before each test
        GameContext.resetInstance();

        // Set up log handler to capture all log messages
        testLogHandler = new TestLogHandler();
        testLogHandler.setLevel(Level.ALL);

        rootLogger = Logger.getLogger("");
        rootLogger.addHandler(testLogHandler);
        rootLogger.setLevel(Level.ALL);
    }

    @AfterEach
    public void tearDown() {
        // Clean up log handler
        if (rootLogger != null && testLogHandler != null) {
            rootLogger.removeHandler(testLogHandler);
        }

        // Reset GameContext after each test
        GameContext.resetInstance();
    }

    @Test
    public void testInitAndStartGameWithDefaultMap() {
        // Given
        GameMapDefinition defaultMap = GameMaps.defaultMap();
        assertNotNull(defaultMap, "Default map should not be null");

        // When
        GameWorldController controller = GameInitializer.initAndStartGame(defaultMap);

        // Then
        assertNotNull(controller, "Game controller should be initialized");
        assertNoErrorsInLogs();
        assertGameContextIsInitialized();
    }

    @Test
    public void testInitAndStartGameWithoutExplicitMap() {
        // When
        GameWorldController controller = GameInitializer.initAndStartGame();

        // Then
        assertNotNull(controller, "Game controller should be initialized");
        assertNoErrorsInLogs();
        assertGameContextIsInitialized();
    }

    @Test
    public void testInitAndStartGameWithAllAvailableMaps() {
        // Given
        List<GameMapDefinition> availableMaps = GameMaps.availableMaps();
        assertFalse(availableMaps.isEmpty(), "There should be at least one available map");

        // When & Then - Test each available map
        for (GameMapDefinition mapDefinition : availableMaps) {
            // Reset for each map
            GameContext.resetInstance();
            testLogHandler.clearRecords();

            // Initialize and start game with the map
            GameWorldController controller = GameInitializer.initAndStartGame(mapDefinition);

            // Validate
            assertNotNull(controller, "Game controller should be initialized for map: " + mapDefinition.getDisplayName());
            assertNoErrorsInLogs("Map: " + mapDefinition.getDisplayName());
            assertGameContextIsInitialized();

            // Clean up for next iteration
            GameContext.resetInstance();
        }
    }

    @Test
    public void testHeroIsInitialized() {
        // When
        GameWorldController controller = GameInitializer.initAndStartGame();

        // Then
        assertNotNull(controller, "Game controller should not be null");
        assertNotNull(controller.getHero(), "Hero should be initialized");
        assertTrue(controller.getHero().getHealth() > 0, "Hero should have health");
    }

    @Test
    public void testTileMapIsInitialized() {
        // When
        GameWorldController controller = GameInitializer.initAndStartGame();

        // Then
        assertNotNull(controller, "Game controller should not be null");
        assertNotNull(controller.getTileMap(), "TileMap should be initialized");
        assertTrue(controller.getTileMap().getWidth() > 0, "TileMap should have width");
        assertTrue(controller.getTileMap().getHeight() > 0, "TileMap should have height");
    }

    @Test
    public void testEnemiesAreInitialized() {
        // When
        GameWorldController controller = GameInitializer.initAndStartGame();

        // Then
        assertNotNull(controller, "Game controller should not be null");
        assertNotNull(controller.getEnemies(), "Enemies collection should be initialized");
        // Note: Not all maps have enemies, so we just check that the collection is not null
    }

    private void assertNoErrorsInLogs() {
        assertNoErrorsInLogs("");
    }

    private void assertNoErrorsInLogs(String context) {
        List<LogRecord> errorRecords = testLogHandler.getRecordsAtLevel(Level.SEVERE);
        List<LogRecord> warningRecords = testLogHandler.getRecordsAtLevel(Level.WARNING);

        String errorMessage = context.isEmpty() ? "" : context + " - ";

        if (!errorRecords.isEmpty()) {
            StringBuilder sb = new StringBuilder(errorMessage + "Found SEVERE log messages:\n");
            for (LogRecord record : errorRecords) {
                sb.append("  - ").append(record.getMessage());
                if (record.getThrown() != null) {
                    sb.append(" (Exception: ").append(record.getThrown().getMessage()).append(")");
                }
                sb.append("\n");
            }
            fail(sb.toString());
        }

        // We can check warnings but not fail on them - just log them
        if (!warningRecords.isEmpty()) {
            System.out.println(errorMessage + "Found WARNING log messages:");
            for (LogRecord record : warningRecords) {
                System.out.println("  - " + record.getMessage());
                if (record.getThrown() != null) {
                    System.out.println("    Exception: " + record.getThrown().getMessage());
                }
            }
        }
    }

    private void assertGameContextIsInitialized() {
        assertDoesNotThrow(() -> {
            GameContext context = GameContext.getInstance();
            assertNotNull(context, "GameContext should be initialized");
            assertNotNull(context.getTileMap(), "GameContext should have a TileMap");
            assertNotNull(context.getHero(), "GameContext should have a Hero");
        }, "GameContext should be properly initialized");
    }

    /**
     * Custom log handler to capture log records during test execution
     */
    private static class TestLogHandler extends Handler {
        private final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
            // Nothing to flush
        }

        @Override
        public void close() throws SecurityException {
            records.clear();
        }

        public List<LogRecord> getRecords() {
            return new ArrayList<>(records);
        }

        public List<LogRecord> getRecordsAtLevel(Level level) {
            List<LogRecord> result = new ArrayList<>();
            for (LogRecord record : records) {
                if (record.getLevel().intValue() >= level.intValue()) {
                    result.add(record);
                }
            }
            return result;
        }

        public void clearRecords() {
            records.clear();
        }
    }
}

