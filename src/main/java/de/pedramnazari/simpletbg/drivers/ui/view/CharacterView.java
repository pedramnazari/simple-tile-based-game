package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

public class CharacterView<T extends ICharacter> extends TileMapElementView<T> {

    private static final double LOW_HEALTH_THRESHOLD = 0.5;
    private static final double MEDIUM_HEALTH_THRESHOLD = 0.8;

    private final ProgressBar healthBar;
    private final StackPane container;

    public CharacterView(T character, Image tileImage, int tileSize) {
        super(character, tileImage, tileSize);

        healthBar = createHealthBar(tileSize);
        container = createContainer(tileSize);

        updateHealthBar();
    }

    private ProgressBar createHealthBar(int tileSize) {
        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(tileSize * 0.9);
        bar.setMinWidth(tileSize * 0.6);
        bar.setMaxWidth(tileSize);
        bar.setPrefHeight(6);
        bar.setMaxHeight(6);
        bar.setMouseTransparent(true);
        bar.setStyle("-fx-accent: " + getColorForProgress(1.0) + ";");
        return bar;
    }

    private StackPane createContainer(int tileSize) {
        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(tileSize, tileSize);
        stackPane.setMinSize(tileSize, tileSize);
        stackPane.setMaxSize(tileSize, tileSize);

        stackPane.getChildren().addAll(getImageView(), healthBar);
        StackPane.setAlignment(getImageView(), Pos.CENTER);
        StackPane.setAlignment(healthBar, Pos.TOP_CENTER);
        StackPane.setMargin(healthBar, new Insets(2, 0, 0, 0));

        return stackPane;
    }

    private double getHealthProgress() {
        return Math.max(0.0, Math.min(1.0, getTileMapElement().getHealth() / 100.0));
    }

    private String getColorForProgress(double progress) {
        if (progress <= LOW_HEALTH_THRESHOLD) {
            return "red";
        }
        if (progress <= MEDIUM_HEALTH_THRESHOLD) {
            return "yellow";
        }
        return "green";
    }

    public void updateHealthBar() {
        double progress = getHealthProgress();
        healthBar.setProgress(progress);
        healthBar.setStyle("-fx-accent: " + getColorForProgress(progress) + ";");
    }

    @Override
    public Node getNode() {
        return container;
    }
}

