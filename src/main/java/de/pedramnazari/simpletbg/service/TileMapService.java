package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;

public class TileMapService {

    private final ITileFactory tileFactory;
    private final IItemFactory itemFactory;
    private final Hero hero;
    private MapNavigator mapNavigator;
    private String currentMapIndex;

    // Maps
    private TileMap tileMap;
    private Collection<Item> items;
    private TileMap enemyMap;

    public TileMapService(ITileFactory tileFactory, IItemFactory itemFactory, final Hero hero) {
        this.tileFactory = tileFactory;
        this.itemFactory = itemFactory;
        this.hero = hero;
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, TileMapConfig itemConfig) {
        this.items = itemFactory.createItemsUsingTileMapConfig(itemConfig);
        return this.createAndInitMap(mapConfig);
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig) {
        Objects.requireNonNull(mapConfig);

        // TODO: use factory to create map
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
        final int oldX = hero.getX();
        final int oldY = hero.getY();

        result.setOldX(oldX);
        result.setOldY(oldY);

        int newX = oldX;
        int newY = oldY;


        switch (moveDirections) {
            case UP -> newY = oldY - 1;
            case DOWN -> newY = oldY + 1;
            case LEFT -> newX = oldX - 1;
            case RIGHT -> newX = oldX + 1;
        }

        result.setNewX(newX);
        result.setNewY(newY);

        if (hasPositionChanged(oldX, newX, oldY, newY)) {
            result.setHasMoved(false);
            return result;
        }


        if (isPositionWithinBoundsOfCurrentMap(newX, newY)) {
            final Tile newTile = tileMap.getTile(newX, newY);
            if (newTile.isObstacle()) {
                result.setHasMoved(false);
                return result;
            }

            result.setHasMoved(true);

            if (items != null) {
                final Optional<Item> optItem = getItem(newX, newY);
                if (optItem.isPresent()) {
                    final Item item = optItem.get();
                    System.out.println("Found item: " + item.getName());
                    hero.getInventory().addItem(item);
                    items.remove(item);

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

    private boolean hasPositionChanged(int oldX, int newX, int oldY, int newY) {
        return (newX == oldX) && (newY == oldY);
    }

    private boolean isPositionWithinBoundsOfCurrentMap(int newX, int newY) {
        // Current assumption: all maps have the same side
        // TODO: in future, check whether figure can be placed to this position (e.g., that there is no rock)
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }

    public Collection<Item> getItems() {
        return List.copyOf(items);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Hero getHero() {
        return hero;
    }

    private Optional<Item> getItem(int x, int y) {
        for (Item item : items) {
            if ((item.getX() == x) && (item.getY() == y)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
