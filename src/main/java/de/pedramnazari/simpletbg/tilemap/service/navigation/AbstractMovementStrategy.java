package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractMovementStrategy implements IMovementStrategy {

    private final CollisionDetectionService collisionDetectionService;

    @Override
    public abstract Point calcNextMove(final TileMap tileMap, final IMovableTileElement element);

    public AbstractMovementStrategy(CollisionDetectionService collisionDetectionService) {
        this.collisionDetectionService = collisionDetectionService;
    }

    protected Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, int currentX, int currentY) {
        final Set<Point> validPositions = new HashSet<>();
        for (MoveDirection moveDirection : MoveDirection.values()) {
            Point newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, moveDirection).orElse(null);

            if (newPosition != null) {
                validPositions.add(newPosition);
            }
        }

        return validPositions;
    }

    protected Optional<Point> calcValidMovePositionWithinMapForDirection(TileMap tileMap, int currentX, int currentY, MoveDirection moveDirection) {
        final Point newPosition = calcNewPosition(moveDirection, currentX, currentY);

        if (isPositionWithinBoundsOfMap(tileMap, newPosition.getX(), newPosition.getY())) {
            if (!collisionDetectionService.isCollisionWithObstacle(tileMap, newPosition.getX(), newPosition.getY())) {
                return Optional.of(newPosition);
            }
        }

        return Optional.empty();
    }

    protected Point calcNewPosition(final MoveDirection moveDirection, int oldX, int oldY) {
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

    protected boolean isPositionWithinBoundsOfMap(final TileMap tileMap, final int newX, final int newY) {
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }

}
