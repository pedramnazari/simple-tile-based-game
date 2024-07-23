package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IBomb;
import javafx.scene.image.Image;

public class BombView extends TileMapElementView<IBomb>{

    private final boolean isExplosion;

    public BombView(IBomb bomb, Image tileImage, boolean isExplosion, int tileSize) {
        super(bomb, tileImage, tileSize);
        this.isExplosion = isExplosion;
    }

    public boolean isExplosion() {
        return isExplosion;
    }
}
