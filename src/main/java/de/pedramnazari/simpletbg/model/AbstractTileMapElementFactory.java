package de.pedramnazari.simpletbg.model;

import de.pedramnazari.simpletbg.service.TileMapConfig;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTileMapElementFactory<T extends ITileMapElement> implements ITileMapElementFactory<T> {

    @Override
    public T createElement(int type, int x, int y) {
        if (type == ITileMapElement.EMPTY_TILE_TYPE) {
            return null;
        }

        return createElementForNonEmptyTile(type, x, y);
    }

    protected abstract T createElementForNonEmptyTile(int type, int x, int y);

    @Override
    public Collection<T> createElementsUsingTileMapConfig(TileMapConfig mapConfig) {
        final Collection<T> elements = new ArrayList<>();

        final int[][] map = mapConfig.getMap();

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
