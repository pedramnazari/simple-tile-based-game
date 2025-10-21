package de.pedramnazari.simpletbg.drivers.ui.view;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class TileMapElementAnimator {

    private final Map<ImageView, TranslateTransition> activeTransitions = new HashMap<>();
    private final Duration animationDuration;
    private final int tileSize;

    TileMapElementAnimator(Duration animationDuration, int tileSize) {
        this.animationDuration = Objects.requireNonNull(animationDuration, "animationDuration must not be null");
        this.tileSize = tileSize;
    }

    void animateMovement(GridPane gridPane, TileMapElementView<?> elementView, int targetTileX, int targetTileY) {
        Objects.requireNonNull(gridPane, "gridPane must not be null");
        Objects.requireNonNull(elementView, "elementView must not be null");

        final ImageView imageView = elementView.getImageView();

        finishActiveTransition(imageView, elementView);

        final int startTileX = elementView.getTileX();
        final int startTileY = elementView.getTileY();

        if (startTileX == targetTileX && startTileY == targetTileY) {
            GridPane.setColumnIndex(imageView, targetTileX);
            GridPane.setRowIndex(imageView, targetTileY);
            return;
        }

        GridPane.setColumnIndex(imageView, startTileX);
        GridPane.setRowIndex(imageView, startTileY);

        final TranslateTransition transition = new TranslateTransition(animationDuration, imageView);
        transition.setToX((targetTileX - startTileX) * tileSize);
        transition.setToY((targetTileY - startTileY) * tileSize);

        elementView.setTilePosition(targetTileX, targetTileY);

        transition.setOnFinished(event -> {
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
            GridPane.setColumnIndex(imageView, elementView.getTileX());
            GridPane.setRowIndex(imageView, elementView.getTileY());
            activeTransitions.remove(imageView);
        });

        activeTransitions.put(imageView, transition);
        transition.play();
    }

    void cancelAnimation(TileMapElementView<?> elementView) {
        finishActiveTransition(elementView.getImageView(), elementView);
    }

    private void finishActiveTransition(ImageView imageView, TileMapElementView<?> elementView) {
        final TranslateTransition activeTransition = activeTransitions.remove(imageView);
        if (activeTransition != null) {
            activeTransition.stop();
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
            GridPane.setColumnIndex(imageView, elementView.getTileX());
            GridPane.setRowIndex(imageView, elementView.getTileY());
        }
    }
}
