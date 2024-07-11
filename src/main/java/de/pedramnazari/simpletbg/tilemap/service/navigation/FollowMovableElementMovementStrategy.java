package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.*;

public class FollowMovableElementMovementStrategy extends AbstractMovementStrategy{

    private final ICharacterProvider<? extends ICharacter> characterProvider;

    public FollowMovableElementMovementStrategy(CollisionDetectionService collisionDetectionService, ICharacterProvider<? extends ICharacter> characterProvider) {
        super(collisionDetectionService);
        this.characterProvider = characterProvider;
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
        final ICharacter targetCharacter = characterProvider.getCharacter();

        if (targetCharacter == null) {
            throw new IllegalStateException("No target character found");
        }

        Point newPosition = null;

        int currentX = element.getX();
        int currentY = element.getY();

        if (currentX < targetCharacter.getX()) {
            newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, MoveDirection.RIGHT).orElse(null);
        } else if (currentX > targetCharacter.getX()) {
            newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, MoveDirection.LEFT).orElse(null);
        }

        if (newPosition == null) {
            if (currentY < targetCharacter.getY()) {
                newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, MoveDirection.DOWN).orElse(null);
            } else if (currentY > targetCharacter.getY()) {
                newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, MoveDirection.UP).orElse(null);
            }
        }

        if (newPosition == null) {
            newPosition = new Point(currentX, currentY);
        }

        return newPosition;
    }
}
