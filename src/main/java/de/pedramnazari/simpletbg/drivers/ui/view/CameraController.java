package de.pedramnazari.simpletbg.drivers.ui.view;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Controls smooth camera movement to follow the hero.
 * Uses interpolation to smoothly track the hero's rendered position.
 */
public class CameraController {
    private final StackPane stackPane;
    private final double viewportWidth;
    private final double viewportHeight;
    private final double mapPixelWidth;
    private final double mapPixelHeight;
    
    // Smoothing factor for camera interpolation (0 = no smoothing, 1 = instant)
    private static final double CAMERA_LERP_FACTOR = 0.25;
    
    private double currentCameraX = 0;
    private double currentCameraY = 0;

    public CameraController(StackPane stackPane, double viewportWidth, double viewportHeight, 
                           double mapPixelWidth, double mapPixelHeight) {
        this.stackPane = stackPane;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.mapPixelWidth = mapPixelWidth;
        this.mapPixelHeight = mapPixelHeight;
    }

    /**
     * Update camera to follow a node, tracking its actual rendered position.
     * This includes any active translate transformations from animations.
     */
    public void updateCameraToFollowNode(Node heroNode, int heroTileX, int heroTileY, int tileSize) {
        if (stackPane == null || heroNode == null) {
            return;
        }

        // Get hero's actual rendered position including animation translate
        double heroRenderX = heroTileX * tileSize + heroNode.getTranslateX() + tileSize / 2.0;
        double heroRenderY = heroTileY * tileSize + heroNode.getTranslateY() + tileSize / 2.0;

        // Calculate target camera position (center viewport on hero)
        double targetTranslateX = viewportWidth / 2.0 - heroRenderX;
        double targetTranslateY = viewportHeight / 2.0 - heroRenderY;

        // Apply clamping to keep map within viewport bounds
        double minTranslateX = Math.min(0, viewportWidth - mapPixelWidth);
        double maxTranslateX = Math.max(0, viewportWidth - mapPixelWidth);
        double minTranslateY = Math.min(0, viewportHeight - mapPixelHeight);
        double maxTranslateY = Math.max(0, viewportHeight - mapPixelHeight);

        targetTranslateX = clamp(targetTranslateX, minTranslateX, maxTranslateX);
        targetTranslateY = clamp(targetTranslateY, minTranslateY, maxTranslateY);

        // Smooth interpolation for fluid camera movement
        currentCameraX = lerp(currentCameraX, targetTranslateX, CAMERA_LERP_FACTOR);
        currentCameraY = lerp(currentCameraY, targetTranslateY, CAMERA_LERP_FACTOR);

        stackPane.setTranslateX(currentCameraX);
        stackPane.setTranslateY(currentCameraY);
    }

    /**
     * Initialize camera position without smoothing (for first frame)
     */
    public void initializeCameraPosition(Node heroNode, int heroTileX, int heroTileY, int tileSize) {
        if (stackPane == null || heroNode == null) {
            return;
        }

        double heroRenderX = heroTileX * tileSize + tileSize / 2.0;
        double heroRenderY = heroTileY * tileSize + tileSize / 2.0;

        double targetTranslateX = viewportWidth / 2.0 - heroRenderX;
        double targetTranslateY = viewportHeight / 2.0 - heroRenderY;

        double minTranslateX = Math.min(0, viewportWidth - mapPixelWidth);
        double maxTranslateX = Math.max(0, viewportWidth - mapPixelWidth);
        double minTranslateY = Math.min(0, viewportHeight - mapPixelHeight);
        double maxTranslateY = Math.max(0, viewportHeight - mapPixelHeight);

        currentCameraX = clamp(targetTranslateX, minTranslateX, maxTranslateX);
        currentCameraY = clamp(targetTranslateY, minTranslateY, maxTranslateY);

        stackPane.setTranslateX(currentCameraX);
        stackPane.setTranslateY(currentCameraY);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private double lerp(double current, double target, double factor) {
        return current + (target - current) * factor;
    }
}
