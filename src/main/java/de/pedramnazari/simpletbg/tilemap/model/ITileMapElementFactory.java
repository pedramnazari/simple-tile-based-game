package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Collection;

public interface ITileMapElementFactory<T extends ITileMapElement> {

    // TODO: change return type to Optional<T>?
    T createElement(int type, int x, int y);

    // TODO: Move this method to Parsers. Here, only Tile[][] should be used.
    Collection<T> createElementsUsingTileMapConfig(int[][] config);
}
