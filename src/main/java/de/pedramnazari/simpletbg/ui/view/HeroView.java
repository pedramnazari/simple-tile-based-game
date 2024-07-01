package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import javafx.scene.image.Image;

public class HeroView extends TileMapElementView<Hero> {

    public HeroView(Hero tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
