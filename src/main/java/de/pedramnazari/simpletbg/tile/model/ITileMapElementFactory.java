package de.pedramnazari.simpletbg.tile.model;

import java.util.Collection;

public interface ITileMapElementFactory<T extends ITileMapElement> {

    // TODO: change return type to Optional<T>?
    T createElement(int type, int x, int y);

    Collection<T> createElementsUsingTileMapConfig(int[][] config);
}
