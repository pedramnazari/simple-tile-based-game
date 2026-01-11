package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IHero;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HeroView extends CharacterView<IHero> {

    private static final double MANA_BAR_WIDTH_RATIO = 0.85;
    private static final double MANA_BAR_HEIGHT = 6.0;
    private final Rectangle manaBarBackground;
    private final Rectangle manaBarFill;
    private final double manaBarWidth;

    public HeroView(IHero hero, Image tileImage, int tileSize) {
        super(hero, tileImage, tileSize);
        
        StackPane container = (StackPane) getDisplayNode();
        
        manaBarWidth = tileSize * MANA_BAR_WIDTH_RATIO;
        
        manaBarBackground = new Rectangle(manaBarWidth, MANA_BAR_HEIGHT);
        manaBarBackground.setArcWidth(MANA_BAR_HEIGHT);
        manaBarBackground.setArcHeight(MANA_BAR_HEIGHT);
        manaBarBackground.setFill(Color.rgb(0, 0, 0, 0.6));
        
        manaBarFill = new Rectangle(manaBarWidth, MANA_BAR_HEIGHT);
        manaBarFill.setArcWidth(MANA_BAR_HEIGHT);
        manaBarFill.setArcHeight(MANA_BAR_HEIGHT);
        manaBarFill.setFill(Color.web("#3498db"));
        
        Pane manaBarPane = new Pane();
        manaBarPane.setMouseTransparent(true);
        manaBarPane.setPickOnBounds(false);
        manaBarPane.setPrefWidth(tileSize);
        manaBarPane.setPrefHeight(MANA_BAR_HEIGHT);
        
        double xOffset = (tileSize - manaBarWidth) / 2.0;
        manaBarBackground.setLayoutX(xOffset);
        manaBarFill.setLayoutX(xOffset);
        manaBarBackground.setLayoutY(0);
        manaBarFill.setLayoutY(0);
        
        manaBarPane.getChildren().addAll(manaBarBackground, manaBarFill);
        
        container.getChildren().add(manaBarPane);
        StackPane.setAlignment(manaBarPane, Pos.TOP_CENTER);
        manaBarPane.setTranslateY(-tileSize / 2.0 + MANA_BAR_HEIGHT * 3);
        
        updateManaBar();
    }

    public void updateManaBar() {
        IHero hero = getTileMapElement();
        double percentage = Math.max(0, Math.min(1, (double) hero.getMana() / hero.getMaxMana()));
        manaBarFill.setWidth(manaBarWidth * percentage);
    }

    @Override
    public void updateHealthBar() {
        super.updateHealthBar();
        updateManaBar();
    }
}

