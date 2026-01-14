package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.ICompanion;
import javafx.scene.image.Image;

public class CompanionView extends CharacterView<ICompanion> {
    public CompanionView(ICompanion companion, Image tileImage, int tileSize) {
        super(companion, tileImage, tileSize);
    }
}
