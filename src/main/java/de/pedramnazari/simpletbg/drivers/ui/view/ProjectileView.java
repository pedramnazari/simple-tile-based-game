package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.Glow;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ProjectileView extends TileMapElementView<IProjectile> {

    private StackPane effectPane;

    public ProjectileView(IProjectile projectile, Image tileImage, int tileSize) {
        super(projectile, tileImage, tileSize);
        
        // Create effect pane with image and visual effects
        effectPane = new StackPane();
        effectPane.getChildren().add(imageView);
        
        // Apply visual effects based on projectile type
        applyProjectileEffect(projectile);
        
        // Set the effect pane as the display node
        setDisplayNode(effectPane);
    }

    private void applyProjectileEffect(IProjectile projectile) {
        int projectileType = projectile.getType();
        
        if (projectileType == TileType.PROJECTILE_ICE.getType()) {
            applyIceEffect();
        } else if (projectileType == TileType.PROJECTILE_LIGHTNING.getType()) {
            applyLightningEffect();
        } else if (projectileType == TileType.PROJECTILE_FIRE.getType()) {
            applyFireEffect();
        }
    }

    /**
     * Apply ice effect: blue glow with frost particles
     */
    private void applyIceEffect() {
        // Add blue glow effect
        Glow glow = new Glow(0.8);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.CYAN);
        dropShadow.setRadius(15);
        dropShadow.setSpread(0.6);
        glow.setInput(dropShadow);
        imageView.setEffect(glow);
        
        // Add frost particle trail (simulated with circles)
        Circle frostParticle = new Circle(3, Color.LIGHTBLUE);
        frostParticle.setOpacity(0.7);
        effectPane.getChildren().add(0, frostParticle);
        
        // Animate the frost particle
        FadeTransition fade = new FadeTransition(Duration.millis(300), frostParticle);
        fade.setFromValue(0.7);
        fade.setToValue(0.0);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.play();
    }

    /**
     * Apply lightning effect: flickering electric appearance
     */
    private void applyLightningEffect() {
        // Add yellow/white glow with flickering
        Glow glow = new Glow(1.0);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.8);
        glow.setInput(dropShadow);
        imageView.setEffect(glow);
        
        // Add electric spark effect
        Circle spark = new Circle(4, Color.LIGHTYELLOW);
        spark.setOpacity(0.9);
        effectPane.getChildren().add(0, spark);
        
        // Animate flickering
        FadeTransition fade = new FadeTransition(Duration.millis(100), spark);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();
        
        // Add scale pulse
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), spark);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.3);
        scale.setToY(1.3);
        scale.setCycleCount(ScaleTransition.INDEFINITE);
        scale.setAutoReverse(true);
        scale.play();
    }

    /**
     * Apply fire effect: orange/red glow
     */
    private void applyFireEffect() {
        // Add orange/red glow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.ORANGE);
        dropShadow.setRadius(12);
        dropShadow.setSpread(0.5);
        imageView.setEffect(dropShadow);
    }
}
