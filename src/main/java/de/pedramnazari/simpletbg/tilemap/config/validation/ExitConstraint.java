package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.List;

class ExitConstraint implements MapConstraint {

    @Override
    public void validate(MapValidationContext context) {
        final List<MapValidationContext.MapPosition> exits = context.findMapTiles(TileType.EXIT.getType());
        final List<MapValidationContext.MapPosition> hiddenExits = context.findMapTiles(TileType.WALL_HIDING_EXIT.getType());

        final int exitCount = exits.size() + hiddenExits.size();
        if (exitCount != 1) {
            throw new IllegalStateException("Exactly one exit tile must exist on the map");
        }

        exits.forEach(position -> assertAccessible(context, position, "Exit"));
        hiddenExits.forEach(position -> assertAccessible(context, position, "Hidden exit"));
    }

    private void assertAccessible(MapValidationContext context, MapValidationContext.MapPosition position, String label) {
        if (isFullySurroundedByDestructibleWalls(context, position)) {
            throw new IllegalStateException(label + " at (" + position.column() + "," + position.row() + ") must not be fully surrounded by destroyable walls");
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
