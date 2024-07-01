package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import javafx.scene.image.Image;

public class EnemyView extends TileMapElementView<Enemy>{
    public EnemyView(Enemy tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
