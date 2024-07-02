package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface IMovementStrategy {

    Point calcNextMove(final TileMap tileMap, final IMoveableTileElement element);

    CollisionDetectionService getCollisionDetectionService();

    default Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, int currentX, int currentY) {
        final Set<Point> validPositions = new HashSet<>();
        for (MoveDirection moveDirection : MoveDirection.values()) {
            Point newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, moveDirection).orElse(null);

            if (newPosition != null) {
                validPositions.add(newPosition);
            }
        }

        return validPositions;
    }

    default Optional<Point> calcValidMovePositionWithinMapForDirection(TileMap tileMap, int currentX, int currentY, MoveDirection moveDirection) {
        final Point newPosition = calcNewPosition(moveDirection, currentX, currentY);

        if (isPositionWithinBoundsOfMap(tileMap, newPosition.getX(), newPosition.getY())) {
            if (!getCollisionDetectionService().isCollisionWithObstacle(tileMap, newPosition.getX(), newPosition.getY())) {
                return Optional.of(newPosition);
            }
        }

        return Optional.empty();
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

    default boolean isPositionWithinBoundsOfMap(final TileMap tileMap, final int newX, final int newY) {
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }
}