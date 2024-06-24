package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.Objects;

public class TileMapService {

    private final ITileFactory tileFactory;
    private final Hero hero;
    private TileMap tileMap;
    private MapNavigator mapNavigator;
    private String currentMapIndex;
    private TileMap itemMap;

    public TileMapService(ITileFactory tileFactory, final Hero hero) {
        this.tileFactory = tileFactory;
        this.hero = hero;
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, TileMapConfig itemConfig) {
        this.itemMap = new TileMap(tileFactory, itemConfig.getMapId(), itemConfig.getMap());
        return this.createAndInitMap(mapConfig);
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig) {
        Objects.requireNonNull(mapConfig);

        this.tileMap = new TileMap(tileFactory, mapConfig.getMapId(), mapConfig.getMap());

        return tileMap;
    }

    public TileMap createAndInitMap(MapNavigator mapNavigator, final String idOfStartingMap) {
        this.mapNavigator = mapNavigator;
        this.currentMapIndex = idOfStartingMap;

        this.tileMap = mapNavigator.getMap(idOfStartingMap);

        return tileMap;
    }

    public MovementResult moveHero(MoveDirections moveDirections) {
        Objects.requireNonNull(moveDirections);

        final MovementResult result = new MovementResult();

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
            final Tile newTile = tileMap.getTile(newX, newY);
            if (newTile.isObstacle()) {
                result.setHasMoved(false);
                return result;
            }

            result.setHasMoved(true);

            if (itemMap != null) {
                final Tile itemTile = itemMap.getTile(newX, newY);
                if ((itemTile != null) && itemTile.isItem()) {
                    final Item item = itemTile.getItem();
                    System.out.println("Found item: " + item.getName());
                    hero.getInventory().addItem(item);
                    itemTile.setItem(null);

                    result.setItem(item);
                }
            }

            hero.setX(newX);
            hero.setY(newY);
        }
        else {
            // Current assumptions:
            // - all maps have the same side
            // - no obstacles at the border of the map
            // => no need to check whether the new position is an obstacle

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

                    result.hasMoved();
                }
            }
        }

        return result;
    }

    private boolean isPositionWithinBoundsOfCurrentMap(int newX, int newY) {
        // Current assumption: all maps have the same side
        // TODO: in future, check whether figure can be placed to this position (e.g., that there is no rock)
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }

    public TileMap getItemMap() {
        return itemMap;
    }
}
