package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;

public class TileMapService {

    private final ITileFactory tileFactory;
    private final IItemFactory itemFactory;
    private final MovementService movementService;
    private final Hero hero;
    private MapNavigator mapNavigator;
    private String currentMapIndex;

    // Maps
    private TileMap tileMap;
    private Collection<Item> items;
    private TileMap enemyMap;

    public TileMapService(ITileFactory tileFactory, IItemFactory itemFactory, MovementService movementService, final Hero hero) {
        this.tileFactory = tileFactory;
        this.itemFactory = itemFactory;
        this.movementService = movementService;
        this.hero = hero;
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, TileMapConfig itemConfig) {
        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
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
        return moveTileMapElement(this.tileMap, this.items, this.hero, moveDirections);
    }

    private Point calcNewPosition(MoveDirections moveDirections, int oldX, int oldY) {
        int dx = 0;
        int dy = 0;

        switch (moveDirections) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        return new Point(oldX + dx, oldY + dy);
    }

    private MovementResult moveTileMapElement(final TileMap tileMap, final Collection<Item> items, final ITileMapElement element, final MoveDirections moveDirections) {
        Objects.requireNonNull(moveDirections);

        final int oldX = element.getX();
        final int oldY = element.getY();

        final Point newPosition = calcNewPosition(moveDirections, oldX, oldY);
        final int newX = newPosition.getX();
        final int newY = newPosition.getY();

        MovementResult result;

        if (isPositionWithinBoundsOfCurrentMap(newX, newY)) {
            result = moveElementWithinMap(tileMap, items, element, newX, newY);
        }
        else {
            result = moveElementBetweenMaps(tileMap, element, moveDirections);
        }

        return result;
    }

    private MovementResult moveElementBetweenMaps(TileMap tileMap, ITileMapElement element, MoveDirections moveDirections) {
        // Current assumptions:
        // - all maps have the same side
        // - no obstacles at the border of the map
        // => no need to check whether the new position is an obstacle

        final MovementResult result = new MovementResult();
        result.setOldX(element.getX());
        result.setOldY(element.getY());

        if (mapNavigator != null) {
            final String nextMapIndex = mapNavigator.getNextMapId(currentMapIndex, moveDirections);

            if (!nextMapIndex.equals(currentMapIndex)) {
                mapNavigator.getMap(nextMapIndex);
                currentMapIndex = nextMapIndex;

                switch(moveDirections) {
                    case UP -> element.setY(tileMap.getHeight() - 1);
                    case DOWN -> element.setY(0);
                    case LEFT -> element.setX(tileMap.getWidth() - 1);
                    case RIGHT -> element.setX(0);
                }

                result.setNewX(element.getX());
                result.setNewY(element.getY());

                result.setHasMoved(true);
            }
        }

        return result;
    }

    private MovementResult moveElementWithinMap(TileMap tileMap, Collection<Item> items, ITileMapElement element, int newX, int newY) {
        final MovementResult result = new MovementResult();
        result.setOldX(element.getX());
        result.setOldY(element.getY());
        result.setNewX(newX);
        result.setNewY(newY);

        final Tile newTile = tileMap.getTile(newX, newY);
        if (newTile.isObstacle()) {
            result.setHasMoved(false);
            return result;
        }

        result.setHasMoved(true);

        // TODO: Refactor (do not use instanceof)
        if ((element instanceof Hero) && (items != null)) {
            final Optional<Item> optItem = getItem(newX, newY);
            if (optItem.isPresent()) {
                final Item item = optItem.get();
                System.out.println("Found item: " + item.getName());
                ((Hero) element).getInventory().addItem(item);
                items.remove(item);

                result.setItem(item);
            }
        }

        element.setX(newX);
        element.setY(newY);

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
