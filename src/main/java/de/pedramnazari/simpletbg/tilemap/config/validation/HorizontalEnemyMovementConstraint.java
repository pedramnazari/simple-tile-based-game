package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.ArrayList;
import java.util.List;

class HorizontalEnemyMovementConstraint implements MapConstraint {

    @Override
    public List<String> validate(MapValidationContext context) {
        List<String> violations = new ArrayList<>();

        for (int row = 0; row < context.getHeight(); row++) {
            for (int col = 0; col < context.getWidth(); col++) {
                if (context.getEnemyType(row, col) == TileType.ENEMY_LR.getType()) {
                    validateHorizontalMovement(context, row, col, violations);
                }
            }
        }

        return violations;
    }

    private void validateHorizontalMovement(MapValidationContext context, int row, int col, List<String> violations) {
        boolean canMoveLeft = canMoveTo(context, row, col - 1);
        boolean canMoveRight = canMoveTo(context, row, col + 1);

        if (!canMoveLeft && !canMoveRight) {
            violations.add("Horizontal enemy at (" + col + "," + row + ") is blocked by walls on both sides");
        }
    }

    private boolean canMoveTo(MapValidationContext context, int row, int col) {
        return context.isWithinBounds(row, col) && TileTypeClassifier.isWalkable(context.getMapTileType(row, col));
    }
}
