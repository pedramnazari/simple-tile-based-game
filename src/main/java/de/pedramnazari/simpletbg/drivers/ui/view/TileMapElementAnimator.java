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

        final Node viewNode = elementView.getNode();

        finishActiveTransition(viewNode, elementView);

        final int startTileX = elementView.getTileX();
        final int startTileY = elementView.getTileY();

        if (startTileX == targetTileX && startTileY == targetTileY) {
            GridPane.setColumnIndex(viewNode, targetTileX);
            GridPane.setRowIndex(viewNode, targetTileY);
            return;
        }

        GridPane.setColumnIndex(viewNode, startTileX);
        GridPane.setRowIndex(viewNode, startTileY);

        final TranslateTransition transition = new TranslateTransition(animationDuration, viewNode);
        transition.setToX((targetTileX - startTileX) * tileSize);
        transition.setToY((targetTileY - startTileY) * tileSize);

        elementView.setTilePosition(targetTileX, targetTileY);

        transition.setOnFinished(event -> {
            viewNode.setTranslateX(0);
            viewNode.setTranslateY(0);
            GridPane.setColumnIndex(viewNode, elementView.getTileX());
            GridPane.setRowIndex(viewNode, elementView.getTileY());
            activeTransitions.remove(viewNode);
        });

        activeTransitions.put(viewNode, transition);
        transition.play();
    }

    void cancelAnimation(TileMapElementView<?> elementView) {
        finishActiveTransition(elementView.getNode(), elementView);
    }

    private void finishActiveTransition(Node viewNode, TileMapElementView<?> elementView) {
        final TranslateTransition activeTransition = activeTransitions.remove(viewNode);
        if (activeTransition != null) {
            activeTransition.stop();
            viewNode.setTranslateX(0);
            viewNode.setTranslateY(0);
            GridPane.setColumnIndex(viewNode, elementView.getTileX());
            GridPane.setRowIndex(viewNode, elementView.getTileY());
        }
    }
}
