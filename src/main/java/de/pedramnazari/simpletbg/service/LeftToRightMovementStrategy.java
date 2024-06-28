package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.model.MoveDirection;
import de.pedramnazari.simpletbg.model.TileMap;

import static de.pedramnazari.simpletbg.model.MoveDirection.LEFT;

public class LeftToRightMovementStrategy implements IMovementStrategy {
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
