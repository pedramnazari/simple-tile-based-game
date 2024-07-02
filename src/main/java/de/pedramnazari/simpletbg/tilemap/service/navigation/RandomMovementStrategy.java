package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomMovementStrategy extends AbstractMovementStrategy {

    public RandomMovementStrategy(CollisionDetectionService collisionDetectionService) {
        super(collisionDetectionService);
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
        int currentX = element.getX();
        int currentY = element.getY();

        final Set<Point> validPositions = calcValidMovePositionsWithinMap(tileMap, currentX, currentY);

        // add also current position (i.e., element does not move)
        validPositions.add(new Point(currentX, currentY));

        final List<Point> possibleNextMoves = new ArrayList<>(validPositions);
        final Random random = new Random();
        int randomIndex = random.nextInt(possibleNextMoves.size());

        return possibleNextMoves.get(randomIndex);
    }
}
