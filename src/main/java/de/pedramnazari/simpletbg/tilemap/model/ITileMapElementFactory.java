package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Collection;

public interface ITileMapElementFactory<T extends ITileMapElement> {

    // TODO: change return type to Optional<T>?
    T createElement(int type, int x, int y);

    // TODO: input should be Tile[][]
    Collection<T> createElementsUsingTileMapConfig(int[][] config);
}
