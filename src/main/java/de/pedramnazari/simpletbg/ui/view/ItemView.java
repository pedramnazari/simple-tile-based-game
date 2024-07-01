package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.inventory.model.Item;
import javafx.scene.image.Image;

public class ItemView extends TileMapElementView<Item>{

    public ItemView(Item tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
