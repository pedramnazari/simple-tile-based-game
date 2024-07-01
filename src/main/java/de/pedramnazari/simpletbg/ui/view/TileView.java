package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.Tile;
import javafx.scene.image.Image;

public class TileView extends TileMapElementView<Tile> {

    public TileView(Tile tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
