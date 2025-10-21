package de.pedramnazari.simpletbg.drivers.ui.view;

import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileMapElementView<T extends ITileMapElement> {

    private final T tileMapElement;
    final ImageView imageView;
    private int tileX;
    private int tileY;

    public TileMapElementView(T tileMapElement, final Image tileImage, int tileSize) {
        this.tileMapElement = tileMapElement;

        imageView = new ImageView(tileImage);
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);

        tileX = tileMapElement.getX();
        tileY = tileMapElement.getY();
    }

    public T getTileMapElement() {
        return tileMapElement;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setTilePosition(int x, int y) {
        tileX = x;
        tileY = y;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setX(int x) {
        setTilePosition(x, tileY);
    }

    public void setY(int y) {
        setTilePosition(tileX, y);
    }

    public int getX() {
        return tileX;
    }

    public int getY() {
        return tileY;
    }

}
