package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;
import de.pedramnazari.simpletbg.tilemap.model.ITileMapElementFactory;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTileMapElementFactory<T extends ITileMapElement> implements ITileMapElementFactory<T> {

    @Override
    public T createElement(int type, int x, int y) {
        if (type == ITileMapElement.EMPTY_TILE_TYPE) {
            return null;
        }

        return createNonEmptyElement(type, x, y);
    }

    // TODO remove at least x and y from the signature
    protected abstract T createNonEmptyElement(int type, int x, int y);

    @Override
    public Collection<T> createElementsUsingTileMapConfig(int[][] map) {
        final Collection<T> elements = new ArrayList<>();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                final T element = createElement(map[row][col], col, row);
                if (element != null) {
                    elements.add(element);
                }
            }
        }

        return elements;
    }
}
