package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import javafx.scene.image.Image;

public class EnemyView extends CharacterView<IEnemy> {
    public EnemyView(IEnemy enemy, Image tileImage, int tileSize) {
        super(enemy, tileImage, tileSize);
    }
}
