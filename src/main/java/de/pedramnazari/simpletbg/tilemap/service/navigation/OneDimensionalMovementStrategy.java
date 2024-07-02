package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

public abstract class OneDimensionalMovementStrategy extends AbstractMovementStrategy {

    private final MoveDirection firstDirection;
    private final MoveDirection secondDirection;

    public OneDimensionalMovementStrategy(MoveDirection firstDirection, MoveDirection secondDirection, CollisionDetectionService collisionDetectionService) {
        super(collisionDetectionService);
        this.firstDirection = firstDirection;
        this.secondDirection = secondDirection;
    }

    public MoveDirection getFirstDirection() {
        return firstDirection;
    }

    public MoveDirection getSecondDirection() {
        return secondDirection;
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMoveableTileElement element) {
        int currentX = element.getX();
        int currentY = element.getY();

        MoveDirection currentDirection = element.getMoveDirection().orElse(null);

        if (currentDirection == null) {
            currentDirection = getFirstDirection();
        }

        Point newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, currentDirection).orElse(null);

        // Current direction is blocked, so try to move in the other direction.
        if (newPosition == null) {
            MoveDirection otherDirection = currentDirection == getFirstDirection() ? getSecondDirection() : getFirstDirection();
            newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, otherDirection).orElse(null);

            // Element seems to be stuck, so stay at current position.
            if (newPosition == null) {
                newPosition = new Point(currentX, currentY);
            }
        }

        return newPosition;
    }

}
