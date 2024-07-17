package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.inventory.model.Bomb;
import javafx.scene.image.Image;

public class BombView extends TileMapElementView<Bomb>{

    public BombView(Bomb bomb, Image tileImage, int tileSize) {
        super(bomb, tileImage, tileSize);
    }
}
