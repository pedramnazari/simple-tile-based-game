package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovementService {
    private static final Logger logger = Logger.getLogger(MovementService.class.getName());

    // TODO: Simplify parameter list
    public MovementResult moveTileMapElement(final TileMap tileMap, final Collection<Item> items,
                                             final IMoveableTileElement element, final MoveDirection moveDirection,
                                             final MapNavigator mapNavigator, final String currentMapIndex) {
        Objects.requireNonNull(moveDirection);

        final int oldX = element.getX();
        final int oldY = element.getY();

        final Point newPosition = calcNewPosition(moveDirection, oldX, oldY);
        final int newX = newPosition.getX();
        final int newY = newPosition.getY();

        MovementResult result;

        if (isPositionWithinBoundsOfCurrentMap(tileMap, newX, newY)) {
            result = moveElementWithinMap(tileMap, items, element, newX, newY, currentMapIndex);
        }
        else {
            result = moveElementBetweenMaps(tileMap, element, moveDirection, mapNavigator, currentMapIndex);
        }

        return result;
    }

    private Point calcNewPosition(final MoveDirection moveDirection, int oldX, int oldY) {
        int dx = 0;
        int dy = 0;

        switch (moveDirection) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        return new Point(oldX + dx, oldY + dy);
    }

    private boolean isPositionWithinBoundsOfCurrentMap(final TileMap tileMap, final int newX, final int newY) {
        // Current assumption: all maps have the same side
        // TODO: in future, check whether figure can be placed to this position (e.g., that there is no rock)
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }

    private MovementResult moveElementWithinMap(TileMap tileMap, Collection<Item> items, IMoveableTileElement element, int newX, int newY, final String currentMapIndex) {
        final MovementResult result = new MovementResult();
        result.setOldX(element.getX());
        result.setOldY(element.getY());
        result.setOldMapIndex(currentMapIndex);

        final Tile newTile = tileMap.getTile(newX, newY);
        if (newTile.isObstacle()) {
            result.setHasMoved(false);
            return result;
        }

        result.setHasMoved(true);

        element.setX(newX);
        element.setY(newY);

        result.setNewX(newX);
        result.setNewY(newY);
        result.setNewMapIndex(currentMapIndex);

        handleItems(items, element, newX, newY, result);

        return result;
    }

    protected void handleItems(Collection<Item> items, IMoveableTileElement element, int newX, int newY, MovementResult result) {
        logger.log(Level.INFO, element.getClass().getSimpleName() + " cannot collect items.");
    }

    protected Optional<Item> getItem(final Collection<Item> items, final int x, final int y) {
        for (Item item : items) {
            if ((item.getX() == x) && (item.getY() == y)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private MovementResult moveElementBetweenMaps(final TileMap tileMap,
                                                  final IMoveableTileElement element,
                                                  final MoveDirection moveDirection,
                                                  final MapNavigator mapNavigator,
                                                  final String currentMapIndex) {
        // Current assumptions:
        // - all maps have the same side
        // - no obstacles at the border of the map
        // => no need to check whether the new position is an obstacle

        final MovementResult result = new MovementResult();
        result.setOldX(element.getX());
        result.setOldY(element.getY());

        if (mapNavigator != null) {
            final String nextMapIndex = mapNavigator.getNextMapId(currentMapIndex, moveDirection);

            if (!nextMapIndex.equals(currentMapIndex)) {
                mapNavigator.getMap(nextMapIndex);
                result.setNewMapIndex(nextMapIndex);

                switch (moveDirection) {
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

    public Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, IMoveableTileElement element) {
        final Set<Point> validPositions = new HashSet<>();

        final int x = element.getX();
        final int y = element.getY();

        for (MoveDirection moveDirection : MoveDirection.values()) {
            final Point newPosition = calcNewPosition(moveDirection, x, y);

            if (isPositionWithinBoundsOfCurrentMap(tileMap, newPosition.getX(), newPosition.getY())) {
                final Tile newTile = tileMap.getTile(newPosition.getX(), newPosition.getY());
                if (!newTile.isObstacle()) {
                    validPositions.add(newPosition);
                }
            }
        }

        return validPositions;
    }

    public boolean isValidMovePositionWithinMap(TileMap tileMap, IMoveableTileElement element, Point newPosition) {
        return calcValidMovePositionsWithinMap(tileMap, element).contains(newPosition);
    }
}
