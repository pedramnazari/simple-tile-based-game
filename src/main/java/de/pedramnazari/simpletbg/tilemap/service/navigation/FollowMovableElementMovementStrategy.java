package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.character.service.ICharacterProvider;
import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

public class FollowMovableElementMovementStrategy extends AbstractMovementStrategy{

    private final ICharacterProvider<? extends Character> characterProvider;

    public FollowMovableElementMovementStrategy(CollisionDetectionService collisionDetectionService, ICharacterProvider<? extends Character> characterProvider) {
        super(collisionDetectionService);
        this.characterProvider = characterProvider;
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
        final Character targetCharacter = characterProvider.getCharacter();

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
