package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IHero;
import javafx.scene.image.Image;

public class HeroView extends CharacterView<IHero> {

    public HeroView(IHero hero, Image tileImage, int tileSize) {
        super(hero, tileImage, tileSize);
    }
}
