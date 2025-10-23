package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class CharacterView<T extends ICharacter> extends TileMapElementView<T> {

    private static final double HEALTH_BAR_WIDTH_RATIO = 0.85;
    private static final double HEALTH_BAR_HEIGHT = 6.0;

    private final StackPane container;
    private final Rectangle healthBarBackground;
    private final Rectangle healthBarFill;
    private final double healthBarWidth;

    protected CharacterView(T character, Image tileImage, int tileSize) {
        super(character, tileImage, tileSize);

        container = new StackPane();
        container.setMinSize(tileSize, tileSize);
        container.setPrefSize(tileSize, tileSize);
        container.setMaxSize(tileSize, tileSize);
        container.getChildren().add(getImageView());

        healthBarWidth = tileSize * HEALTH_BAR_WIDTH_RATIO;

        healthBarBackground = new Rectangle(healthBarWidth, HEALTH_BAR_HEIGHT);
        healthBarBackground.setArcWidth(HEALTH_BAR_HEIGHT);
        healthBarBackground.setArcHeight(HEALTH_BAR_HEIGHT);
        healthBarBackground.setFill(Color.rgb(0, 0, 0, 0.6));

        healthBarFill = new Rectangle(healthBarWidth, HEALTH_BAR_HEIGHT);
        healthBarFill.setArcWidth(HEALTH_BAR_HEIGHT);
        healthBarFill.setArcHeight(HEALTH_BAR_HEIGHT);

        Pane healthBarPane = new Pane();
        healthBarPane.setMouseTransparent(true);
        healthBarPane.setPickOnBounds(false);
        healthBarPane.setPrefWidth(tileSize);
        healthBarPane.setPrefHeight(HEALTH_BAR_HEIGHT);

        double xOffset = (tileSize - healthBarWidth) / 2.0;
        healthBarBackground.setLayoutX(xOffset);
        healthBarFill.setLayoutX(xOffset);
        healthBarBackground.setLayoutY(0);
        healthBarFill.setLayoutY(0);

        healthBarPane.getChildren().addAll(healthBarBackground, healthBarFill);

        container.getChildren().add(healthBarPane);
        StackPane.setAlignment(healthBarPane, Pos.TOP_CENTER);
        healthBarPane.setTranslateY(-tileSize / 2.0 + HEALTH_BAR_HEIGHT);

        setDisplayNode(container);

        updateHealthBar();
    }

    public void updateHealthBar() {
        double percentage = Math.max(0, Math.min(1, getTileMapElement().getHealth() / 100.0));
        healthBarFill.setWidth(healthBarWidth * percentage);
        healthBarFill.setFill(selectColor(percentage));
    }

    private Color selectColor(double percentage) {
        if (percentage >= 0.8) {
            return Color.web("#2ecc71");
        }
        if (percentage >= 0.2) {
            return Color.web("#f1c40f");
        }
        return Color.web("#e74c3c");
    }
}

