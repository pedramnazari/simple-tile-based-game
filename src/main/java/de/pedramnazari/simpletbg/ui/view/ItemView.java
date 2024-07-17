package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IItem;
import javafx.scene.image.Image;

public class ItemView extends TileMapElementView<IItem>{

    public ItemView(IItem item, Image tileImage, int tileSize) {
        super(item, tileImage, tileSize);
    }
}
