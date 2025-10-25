package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.ArrayList;
import java.util.List;

class VerticalEnemyMovementConstraint implements MapConstraint {

    @Override
    public List<String> validate(MapValidationContext context) {
        List<String> violations = new ArrayList<>();

        for (int row = 0; row < context.getHeight(); row++) {
            for (int col = 0; col < context.getWidth(); col++) {
                if (context.getEnemyType(row, col) == TileType.ENEMY_TD.getType()) {
                    validateVerticalMovement(context, row, col, violations);
                }
            }
        }

        return violations;
    }

    private void validateVerticalMovement(MapValidationContext context, int row, int col, List<String> violations) {
        boolean canMoveUp = canMoveTo(context, row - 1, col);
        boolean canMoveDown = canMoveTo(context, row + 1, col);

        if (!canMoveUp && !canMoveDown) {
            violations.add("Vertical enemy at (" + col + "," + row + ") is blocked by walls above and below");
        }
    }

    private boolean canMoveTo(MapValidationContext context, int row, int col) {
        return context.isWithinBounds(row, col) && TileTypeClassifier.isWalkable(context.getMapTileType(row, col));
    }
}
