package de.pedramnazari.simpletbg.tile.service.navigation;

import de.pedramnazari.simpletbg.inventory.service.IItemService;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tile.model.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class MovementService {
    private static final Logger logger = Logger.getLogger(MovementService.class.getName());

    // TODO: Simplify parameter list
    public MovementResult moveElement(IMoveableTileElement element, MoveDirection moveDirection, GameContext gameContext) {
        Objects.requireNonNull(moveDirection);

        final TileMap tileMap = gameContext.getTileMap();
        final IItemService itemService = gameContext.getItemService();
        final String currentMapIndex = gameContext.getCurrentMapIndex();

        final int oldX = element.getX();
        final int oldY = element.getY();

        final Point newPosition = calcNewPosition(moveDirection, oldX, oldY);
        final int newX = newPosition.getX();
        final int newY = newPosition.getY();

        MovementResult result;

        if (isValidMovePositionWithinMap(tileMap, element, newX, newY)) {
            // TODO: isValidMovePositionWithinMap() is also invoked in moveElementToPositionWithinMap()
            result = moveElementToPositionWithinMap(gameContext, element, newX, newY);
            result.setOldMapIndex(currentMapIndex);
            result.setNewMapIndex(currentMapIndex);
        } else if (!isPositionWithinBoundsOfCurrentMap(tileMap, newX, newY)) {
            result = moveElementBetweenMaps(tileMap, element, moveDirection, currentMapIndex);
        } else {
            result = new MovementResult();
            result.setOldX(element.getX());
            result.setOldY(element.getY());
            result.setOldMapIndex(currentMapIndex);
            result.hasElementMoved();
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

    public MovementResult moveElementToPositionWithinMap(final GameContext gameContext, IMoveableTileElement element, int newX, int newY) {
        final MovementResult result = new MovementResult();
        result.setOldX(element.getX());
        result.setOldY(element.getY());

        if (!isValidMovePositionWithinMap(gameContext.getTileMap(), element, newX, newY)) {
            result.setHasElementMoved(false);
            return result;
        }

        Optional<MoveDirection> direction = MoveDirection.getDirection(element.getX(), element.getY(), newX, newY);

        element.setMoveDirection(direction.orElse(null));
        element.setX(newX);
        element.setY(newY);

        result.setNewX(newX);
        result.setNewY(newY);
        result.setHasElementMoved(true);

        handleElementHasMoved(gameContext, element, newX, newY, result);

        return result;
    }

    private static boolean hasPositionChanged(int oldX, int oldY, int newX, int newY) {
        return (oldX == newX) && (oldY == newY);
    }

    protected void handleElementHasMoved(GameContext gameContext, IMoveableTileElement element, int newX, int newY, MovementResult result) {

    }

    private MovementResult moveElementBetweenMaps(final TileMap tileMap,
                                                  final IMoveableTileElement element,
                                                  final MoveDirection moveDirection,
                                                  final String currentMapIndex) {
        // TODO: implement

        final MovementResult result = new MovementResult();
        result.setOldX(element.getX());
        result.setOldY(element.getY());
        result.setHasElementMoved(false);

        return result;
    }

    public Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, int currentX, int currentY) {
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

    public Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, IMoveableTileElement element) {
        return calcValidMovePositionsWithinMap(tileMap, element.getX(), element.getY());
    }

    public boolean isValidMovePositionWithinMap(TileMap tileMap, IMoveableTileElement element, int newX, int newY) {
        return isValidMovePositionWithinMap(tileMap, element.getX(), element.getY(), newX, newY);
    }

    public boolean isValidMovePositionWithinMap(TileMap tileMap, int oldX, int oldY, int newX, int newY) {
        return calcValidMovePositionsWithinMap(tileMap, oldX, oldY).contains(new Point(newX, newY));
    }
}
