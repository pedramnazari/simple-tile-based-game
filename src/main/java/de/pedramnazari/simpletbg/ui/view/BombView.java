package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.inventory.model.Bomb;
import javafx.scene.image.Image;

public class BombView extends TileMapElementView<Bomb>{

    private final boolean isExplosion;

    public BombView(Bomb bomb, Image tileImage, boolean isExplosion, int tileSize) {
        super(bomb, tileImage, tileSize);
        this.isExplosion = isExplosion;
    }

    public boolean isExplosion() {
        return isExplosion;
    }
}
