package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.ArrayList;
import java.util.List;

class ExitConstraint implements MapConstraint {

    @Override
    public List<String> validate(MapValidationContext context) {
        List<String> violations = new ArrayList<>();

        final List<MapValidationContext.MapPosition> exits = context.findMapTiles(TileType.EXIT.getType());
        final List<MapValidationContext.MapPosition> hiddenExits = context.findMapTiles(TileType.WALL_HIDING_EXIT.getType());

        final int exitCount = exits.size() + hiddenExits.size();
        if (exitCount != 1) {
            violations.add("Exactly one exit tile must exist on the map");
        }

        exits.forEach(position -> collectAccessibilityViolation(context, position, "Exit", violations));
        hiddenExits.forEach(position -> collectAccessibilityViolation(context, position, "Hidden exit", violations));

        return violations;
    }

    private void collectAccessibilityViolation(MapValidationContext context,
                                               MapValidationContext.MapPosition position,
                                               String label,
                                               List<String> violations) {
        if (isFullySurroundedByDestructibleWalls(context, position)) {
            violations.add(label + " at (" + position.column() + "," + position.row() + ") must not be fully surrounded by destroyable walls");
        }
    }

    private boolean isFullySurroundedByDestructibleWalls(MapValidationContext context, MapValidationContext.MapPosition position) {
        final int[][] directions = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        int consideredNeighbors = 0;

        for (int[] direction : directions) {
            int neighborRow = position.row() + direction[0];
            int neighborCol = position.column() + direction[1];

            if (context.isWithinBounds(neighborRow, neighborCol)) {
                consideredNeighbors++;
                if (!TileTypeClassifier.isDestructibleWall(context.getMapTileType(neighborRow, neighborCol))) {
                    return false;
                }
            }
        }

        return consideredNeighbors > 0;
    }
}
