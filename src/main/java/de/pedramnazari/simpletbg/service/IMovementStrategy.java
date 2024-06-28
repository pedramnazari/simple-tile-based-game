package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.model.MoveDirection;
import de.pedramnazari.simpletbg.model.Tile;
import de.pedramnazari.simpletbg.model.TileMap;

import java.util.HashSet;
import java.util.Set;

public interface IMovementStrategy {

    Point calcNextMove(final TileMap tileMap, final IMoveableTileElement element);

    default Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, int currentX, int currentY) {
        final Set<Point> validPositions = new HashSet<>();
        for (MoveDirection moveDirection : MoveDirection.values()) {
            final Point newPosition = calcNewPosition(moveDirection, currentX, currentY);

            if (isPositionWithinBoundsOfCurrentMap(tileMap, newPosition.getX(), newPosition.getY())) {
                final Tile newTile = tileMap.getTile(newPosition.getX(), newPosition.getY());
                if (!newTile.isObstacle()) {
                    validPositions.add(newPosition);
                }
            }
        }

        return validPositions;
    }

    default Point calcNewPosition(final MoveDirection moveDirection, int oldX, int oldY) {
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

    default boolean isPositionWithinBoundsOfCurrentMap(final TileMap tileMap, final int newX, final int newY) {
        // Current assumption: all maps have the same side
        // TODO: in future, check whether figure can be placed to this position (e.g., that there is no rock)
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }
}
