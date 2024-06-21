package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.Objects;

public class TileMapService {

    private final Hero hero;
    private TileMap tileMap;
    private MapNavigator mapNavigator;
    private String currentMapIndex;

    public TileMapService(final Hero hero) {
        this.hero = hero;
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig) {
        Objects.requireNonNull(mapConfig);

        this.tileMap = new TileMap(mapConfig.getMapId(), mapConfig.getMap());

        return tileMap;
    }

    public TileMap createAndInitMap(MapNavigator mapNavigator, final String idOfStartingMap) {
        this.mapNavigator = mapNavigator;
        this.currentMapIndex = idOfStartingMap;

        this.tileMap = mapNavigator.getMap(idOfStartingMap);

        return tileMap;
    }

    public void moveHero(MoveDirections moveDirections) {
        Objects.requireNonNull(moveDirections);

        // Calculate new position
        int newX = hero.getX();
        int newY = hero.getY();

        switch (moveDirections) {
            case UP -> newY = newY - 1;
            case DOWN -> newY = newY + 1;
            case LEFT -> newX = newX - 1;
            case RIGHT -> newX = newX + 1;
        }


        if (isPositionWithinBoundsOfCurrentMap(newX, newY)) {
            hero.setX(newX);
            hero.setY(newY);
        }
        else {
            if (mapNavigator != null) {
                final String nextMapIndex = mapNavigator.getNextMapId(currentMapIndex, moveDirections);

                if (!nextMapIndex.equals(currentMapIndex)) {
                    mapNavigator.getMap(nextMapIndex);
                    currentMapIndex = nextMapIndex;

                    switch(moveDirections) {
                        case UP -> hero.setY(tileMap.getHeight() - 1);
                        case DOWN -> hero.setY(0);
                        case LEFT -> hero.setX(tileMap.getWidth() - 1);
                        case RIGHT -> hero.setX(0);
                    }
                }
            }
        }
    }

    private boolean isPositionWithinBoundsOfCurrentMap(int newX, int newY) {
        // Current assumption: all maps have the same side
        // TODO: in future, check whether figure can be placed to this position (e.g., that there is no rock)
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }


}
