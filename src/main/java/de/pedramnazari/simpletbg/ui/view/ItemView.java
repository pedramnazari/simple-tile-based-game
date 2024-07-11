package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IItem;
import javafx.scene.image.Image;

public class ItemView extends TileMapElementView<IItem>{

    public ItemView(IItem tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
