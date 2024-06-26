package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import static de.pedramnazari.simpletbg.tilemap.model.MoveDirection.LEFT;

public class LeftToRightMovementStrategy extends AbstractMovementStrategy {

    public LeftToRightMovementStrategy(CollisionDetectionService collisionDetectionService) {
        super(collisionDetectionService);
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMoveableTileElement element) {
        int currentX = element.getX();
        int currentY = element.getY();

        MoveDirection currentDirection = element.getMoveDirection().orElse(null);

        if (currentDirection == null) {
            currentDirection = LEFT;
        }

        Point newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, currentDirection).orElse(null);

        if (newPosition == null) {
            currentDirection = currentDirection == LEFT ? MoveDirection.RIGHT : MoveDirection.LEFT;
            newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, currentDirection).orElse(null);

            if (newPosition == null) {
                newPosition = new Point(currentX, currentY);
            }
        }

        return newPosition;
    }
}
