package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.List;

class PortalConstraint implements MapConstraint {

    @Override
    public void validate(MapValidationContext context) {
        final List<MapValidationContext.MapPosition> portals = context.findMapTiles(TileType.PORTAL.getType());
        final List<MapValidationContext.MapPosition> hiddenPortals = context.findMapTiles(TileType.WALL_HIDING_PORTAL.getType());

        final int portalCount = portals.size() + hiddenPortals.size();
        if (portalCount != 0 && portalCount != 2) {
            throw new IllegalStateException("There must be no or exactly 2 portals on the map");
        }

        portals.forEach(position -> assertAccessible(context, position, "Portal"));
        hiddenPortals.forEach(position -> assertAccessible(context, position, "Hidden portal"));
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
