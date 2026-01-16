package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Manages game session state for resume functionality and map selection.
 * Ensures new games start with different maps and allows resuming previous sessions.
 */
public class GameSessionManager {

    private static final Logger logger = Logger.getLogger(GameSessionManager.class.getName());

    private static GameSessionManager instance;

    private GameMapDefinition lastPlayedMap;
    private boolean hasResumableSession;
    private final Random random = new Random();

    private GameSessionManager() {
        // Private constructor for singleton
    }

    public static synchronized GameSessionManager getInstance() {
        if (instance == null) {
            instance = new GameSessionManager();
        }
        return instance;
    }

    /**
     * Saves the current session state.
     * 
     * @param mapDefinition The map that was being played
     * @param isResumable Whether this session can be resumed (not ended)
     */
    public void saveSession(GameMapDefinition mapDefinition, boolean isResumable) {
        this.lastPlayedMap = mapDefinition;
        this.hasResumableSession = isResumable;
        logger.info("Session saved: map=" + (mapDefinition != null ? mapDefinition.getDisplayName() : "null") 
                    + ", resumable=" + isResumable);
    }

    /**
     * Clears the saved session (e.g., when starting a new game).
     */
    public void clearSession() {
        this.lastPlayedMap = null;
        this.hasResumableSession = false;
        logger.info("Session cleared");
    }

    /**
     * Returns whether there is a resumable session.
     */
    public boolean hasResumableSession() {
        return hasResumableSession && lastPlayedMap != null;
    }

    /**
     * Gets the map definition for the resumable session.
     */
    public GameMapDefinition getResumableMap() {
        return lastPlayedMap;
    }

    /**
     * Selects a map for a new game, ensuring it's different from the last played map.
     * If there's only one map or the last map is unknown, returns any available map.
     */
    public GameMapDefinition selectNewGameMap() {
        List<GameMapDefinition> availableMaps = GameMaps.availableMaps();
        
        if (availableMaps.isEmpty()) {
            return GameMaps.defaultMap();
        }
        
        if (availableMaps.size() == 1) {
            return availableMaps.get(0);
        }
        
        // If we have a last played map, try to select a different one
        if (lastPlayedMap != null) {
            List<GameMapDefinition> otherMaps = availableMaps.stream()
                .filter(map -> !map.getId().equals(lastPlayedMap.getId()))
                .toList();
            
            if (!otherMaps.isEmpty()) {
                int index = random.nextInt(otherMaps.size());
                return otherMaps.get(index);
            }
        }
        
        // Fallback: select random map from all available
        int index = random.nextInt(availableMaps.size());
        return availableMaps.get(index);
    }

    /**
     * Marks the session as ended (cannot be resumed).
     */
    public void markSessionEnded() {
        this.hasResumableSession = false;
        logger.info("Session marked as ended");
    }
}
