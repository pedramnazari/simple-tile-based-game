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
        if (MapConstraintUtils.isFullySurroundedByDestructibleWalls(context, position)) {
            throw new IllegalStateException(label + " at (" + position.column() + "," + position.row() + ") must not be fully surrounded by destroyable walls");
        }
    }

    // Removed duplicate isFullySurroundedByDestructibleWalls method; now using MapConstraintUtils.
}
