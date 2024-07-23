package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileMapElementView<T extends ITileMapElement> {

    private final T tileMapElement;
    final ImageView imageView;

    public TileMapElementView(T tileMapElement, final Image tileImage, int tileSize) {
        this.tileMapElement = tileMapElement;

        imageView = new ImageView(tileImage);
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);
    }

    public T getTileMapElement() {
        return tileMapElement;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setX(int x) {
        imageView.setX(x);
    }

    public void setY(int y) {
        imageView.setY(y);
    }

    public int getX() {
        return (int) imageView.getX();
    }

    public int getY() {
        return (int) imageView.getY();
    }

}
