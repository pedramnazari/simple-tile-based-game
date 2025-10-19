package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import javafx.scene.image.Image;

public class ProjectileView extends TileMapElementView<IProjectile> {

    public ProjectileView(IProjectile projectile, Image tileImage, int tileSize) {
        super(projectile, tileImage, tileSize);
    }
}
