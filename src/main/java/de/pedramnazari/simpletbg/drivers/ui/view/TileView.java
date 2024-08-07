package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.Tile;
import javafx.scene.image.Image;

public class TileView extends TileMapElementView<Tile> {

    private String imagePath;

    public TileView(Tile tile, Image tileImage, int tileSize) {
        super(tile, tileImage, tileSize);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
