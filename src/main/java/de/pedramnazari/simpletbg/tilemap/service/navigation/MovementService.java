package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IItemService;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class MovementService {
    private static final Logger logger = Logger.getLogger(MovementService.class.getName());

    // TODO: Simplify parameter list
    public MovementResult moveElement(IMovableTileElement element, MoveDirection moveDirection, GameContext gameContext) {
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
        }
        else if (!isPositionWithinBoundsOfCurrentMap(tileMap, newX, newY)) {
            result = moveElementBetweenMaps(tileMap, element, moveDirection, currentMapIndex);
        }
        else {
            result = new MovementResult();
            result.setOldX(element.getX());
            result.setOldY(element.getY());
            result.setOldMapIndex(currentMapIndex);
            result.setHasElementMoved(false);
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
        return (newX >= 0) && (newX < tileMap.getWidth()) && (newY >= 0) && (newY < tileMap.getHeight());
    }

    public MovementResult moveElementToPositionWithinMap(final GameContext gameContext, IMovableTileElement element, int newX, int newY) {
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

    protected void handleElementHasMoved(GameContext gameContext, IMovableTileElement element, int newX, int newY, MovementResult result) {

    }

    private MovementResult moveElementBetweenMaps(final TileMap tileMap,
                                                  final IMovableTileElement element,
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
                if (!newTile.isObstacle() || newTile.isDestroyed()) {
                    validPositions.add(newPosition);
                }
            }
        }

        return validPositions;
    }

    public Set<Point> calcValidMovePositionsWithinMap(TileMap tileMap, IMovableTileElement element) {
        return calcValidMovePositionsWithinMap(tileMap, element.getX(), element.getY());
    }

    public boolean isValidMovePositionWithinMap(TileMap tileMap, IMovableTileElement element, int newX, int newY) {
        return isValidMovePositionWithinMap(tileMap, element.getX(), element.getY(), newX, newY);
    }


    public boolean isValidMovePositionWithinMap(TileMap tileMap, int oldX, int oldY, int newX, int newY) {
        if (calcValidMovePositionsWithinMap(tileMap, oldX, oldY).contains(new Point(newX, newY))) {
            return true;
        }

        if (isPositionWithinBoundsOfCurrentMap(tileMap, newX, newY)) {
            // TODO: add a method such as teleportCharacter(int destinationX, destinationY) to handle teleportation
            return isDestinationPortalTile(tileMap, newX, newY);
        }

        return false;
    }

    private static boolean isDestinationPortalTile(TileMap tileMap, int newX, int newY) {
        final Tile destinationTile = tileMap.getTile(newX, newY);
        return (destinationTile != null) && destinationTile.isPortal();
    }
}
