package de.pedramnazari.simpletbg.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import javafx.scene.image.Image;

public class EnemyView extends TileMapElementView<IEnemy>{
    public EnemyView(IEnemy tileMapElement, Image tileImage, int tileSize) {
        super(tileMapElement, tileImage, tileSize);
    }
}
