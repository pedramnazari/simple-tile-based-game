package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IHero;
import javafx.scene.image.Image;

public class HeroView extends TileMapElementView<IHero> {

    public HeroView(IHero tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
