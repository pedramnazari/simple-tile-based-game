package de.pedramnazari.simpletbg.drivers.ui.view;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class TileMapElementAnimator {

    private final Map<Node, TranslateTransition> activeTransitions = new HashMap<>();
    private final Duration animationDuration;
    private final int tileSize;

    TileMapElementAnimator(Duration animationDuration, int tileSize) {
        this.animationDuration = Objects.requireNonNull(animationDuration, "animationDuration must not be null");
        this.tileSize = tileSize;
    }

    void animateMovement(GridPane gridPane, TileMapElementView<?> elementView, int targetTileX, int targetTileY) {
        Objects.requireNonNull(gridPane, "gridPane must not be null");
        Objects.requireNonNull(elementView, "elementView must not be null");

        final Node node = elementView.getDisplayNode();

        finishActiveTransition(node, elementView);

        final int startTileX = elementView.getTileX();
        final int startTileY = elementView.getTileY();

        if (startTileX == targetTileX && startTileY == targetTileY) {
            GridPane.setColumnIndex(node, targetTileX);
            GridPane.setRowIndex(node, targetTileY);
            return;
        }

        GridPane.setColumnIndex(node, startTileX);
        GridPane.setRowIndex(node, startTileY);

        final TranslateTransition transition = new TranslateTransition(animationDuration, node);
        transition.setToX((targetTileX - startTileX) * tileSize);
        transition.setToY((targetTileY - startTileY) * tileSize);

        elementView.setTilePosition(targetTileX, targetTileY);

        transition.setOnFinished(event -> {
            node.setTranslateX(0);
            node.setTranslateY(0);
            GridPane.setColumnIndex(node, elementView.getTileX());
            GridPane.setRowIndex(node, elementView.getTileY());
            activeTransitions.remove(node);
        });

        activeTransitions.put(node, transition);
        transition.play();
    }

    void cancelAnimation(TileMapElementView<?> elementView) {
        finishActiveTransition(elementView.getDisplayNode(), elementView);
    }

    private void finishActiveTransition(Node node, TileMapElementView<?> elementView) {
        final TranslateTransition activeTransition = activeTransitions.remove(node);
        if (activeTransition != null) {
            activeTransition.stop();
            node.setTranslateX(0);
            node.setTranslateY(0);
            GridPane.setColumnIndex(node, elementView.getTileX());
            GridPane.setRowIndex(node, elementView.getTileY());
        }
    }

    /**
     * Stops all active animations.
     */
    void stopAll() {
        for (TranslateTransition transition : activeTransitions.values()) {
            if (transition != null) {
                transition.stop();
            }
        }
        activeTransitions.clear();
    }
}
