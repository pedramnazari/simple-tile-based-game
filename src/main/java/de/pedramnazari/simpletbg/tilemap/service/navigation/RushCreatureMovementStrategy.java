package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.*;

import java.util.Random;

public class RushCreatureMovementStrategy extends AbstractMovementStrategy {

    private final ICharacterProvider<? extends ICharacter> characterProvider;
    private final Random random;
    
    // Random behavior variations per creature instance
    private final double speedMultiplier;
    private final BehaviorVariation behaviorVariation;
    private int movementCounter = 0;
    
    private enum BehaviorVariation {
        STRAIGHT,      // Direct path to hero
        ZIG_ZAG,       // Alternates direction slightly
        HESITANT,      // Occasionally pauses
        DASH,          // Occasional double movement
        JITTER         // Random small deviations
    }

    public RushCreatureMovementStrategy(CollisionDetectionService collisionDetectionService, 
                                       ICharacterProvider<? extends ICharacter> characterProvider,
                                       long seed) {
        super(collisionDetectionService);
        this.characterProvider = characterProvider;
        this.random = new Random(seed);
        
        // Random speed variation (0.8 to 1.2x)
        this.speedMultiplier = 0.8 + (random.nextDouble() * 0.4);
        
        // Random behavior pattern
        this.behaviorVariation = BehaviorVariation.values()[random.nextInt(BehaviorVariation.values().length)];
    }

    @Override
    public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
        final ICharacter targetCharacter = characterProvider.getCharacter();

        if (targetCharacter == null) {
            throw new IllegalStateException("No target character found");
        }

        movementCounter++;
        
        // Apply behavior variations
        // Note: Rush creatures should always move, so we don't allow them to stay still
        switch (behaviorVariation) {
            case HESITANT:
                // Occasionally hesitate but still move (just in a more random direction)
                if (movementCounter % 4 == 0 && random.nextDouble() < 0.3) {
                    // Instead of stopping, move in a slightly random direction
                    // This will be handled by the jitter logic below
                }
                break;
            case DASH:
                // Occasionally move is processed elsewhere (represents faster movement)
                // For now, just use standard movement
                break;
        }

        int currentX = element.getX();
        int currentY = element.getY();
        Point newPosition = null;

        // Calculate direction to hero
        int dx = targetCharacter.getX() - currentX;
        int dy = targetCharacter.getY() - currentY;

        // Apply behavior-specific modifications
        if (behaviorVariation == BehaviorVariation.ZIG_ZAG) {
            // Alternate between horizontal and vertical movement
            if (movementCounter % 2 == 0) {
                dy = 0; // Only move horizontally
            } else {
                dx = 0; // Only move vertically
            }
        } else if (behaviorVariation == BehaviorVariation.JITTER) {
            // Occasionally move in a random direction instead
            if (random.nextDouble() < 0.2) {
                MoveDirection[] directions = MoveDirection.values();
                MoveDirection randomDir = directions[random.nextInt(directions.length)];
                newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, randomDir).orElse(null);
                if (newPosition != null) {
                    return newPosition;
                }
            }
        }

        // Primary movement logic: move toward hero
        if (dx != 0) {
            MoveDirection direction = dx > 0 ? MoveDirection.RIGHT : MoveDirection.LEFT;
            newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, direction).orElse(null);
        }

        if (newPosition == null && dy != 0) {
            MoveDirection direction = dy > 0 ? MoveDirection.DOWN : MoveDirection.UP;
            newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, direction).orElse(null);
        }

        // If blocked, try alternate routes
        if (newPosition == null) {
            if (dy != 0) {
                MoveDirection direction = dy > 0 ? MoveDirection.DOWN : MoveDirection.UP;
                newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, direction).orElse(null);
            }
        }

        // If still blocked, try all remaining directions to keep moving
        if (newPosition == null) {
            for (MoveDirection direction : MoveDirection.values()) {
                newPosition = calcValidMovePositionWithinMapForDirection(tileMap, currentX, currentY, direction).orElse(null);
                if (newPosition != null) {
                    break; // Found a valid direction, use it
                }
            }
        }

        // Only as last resort, stay in place (e.g., completely surrounded)
        if (newPosition == null) {
            newPosition = new Point(currentX, currentY);
        }

        return newPosition;
    }
}
