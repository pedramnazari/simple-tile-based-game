package de.pedramnazari.simpletbg.tilemap.config.validation;

final class ConstraintUtils {

    private static final int[][] CARDINAL_DIRECTIONS = new int[][]{
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    private ConstraintUtils() {
    }

    static boolean isFullySurroundedByNonWalkableTiles(MapValidationContext context,
                                                       MapValidationContext.MapPosition position) {
        int consideredNeighbors = 0;

        for (int[] direction : CARDINAL_DIRECTIONS) {
            int neighborRow = position.row() + direction[0];
            int neighborCol = position.column() + direction[1];

            if (context.isWithinBounds(neighborRow, neighborCol)) {
                consideredNeighbors++;
                if (TileTypeClassifier.isWalkable(context.getMapTileType(neighborRow, neighborCol))) {
                    return false;
                }
            }
        }

        return consideredNeighbors > 0;
    }
}
