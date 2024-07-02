package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.List;
import java.util.Map;

import static de.pedramnazari.simpletbg.tilemap.model.MoveDirection.*;

public class CircularMovementStrategy extends AbstractMovementStrategy {

    /*
            ------RIGHT---->
           ^               |
           |               |
           UP            DOWN
           |               |
           |               V
           <------LEFT------
    */
        
    private final Map<MoveDirection, List<MoveDirection>> nextMoveMap = Map.of(
            RIGHT,  List.of(RIGHT, DOWN, UP, LEFT),
            DOWN,   List.of(DOWN, LEFT, RIGHT, UP),
            LEFT,   List.of(LEFT, UP, DOWN, RIGHT),
            UP,     List.of(UP, RIGHT, LEFT, DOWN)
    );

    public CircularMovementStrategy(CollisionDetectionService collisionDetectionService) {
        super(collisionDetectionService);
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
        int currentX = element.getX();
        int currentY = element.getY();

        MoveDirection currentDirection = element.getMoveDirection().orElse(RIGHT);

        Point nextPosition = new Point(currentX, currentY);

        for (MoveDirection direction : nextMoveMap.get(currentDirection)) {
            Point newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, direction).orElse(null);

            if (newPosition != null) {
                nextPosition = newPosition;
                break;
            }
        }

        return nextPosition;
    }
}
