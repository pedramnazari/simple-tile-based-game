package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.ArrayList;
import java.util.List;

class PortalConstraint implements MapConstraint {

    @Override
    public List<String> validate(MapValidationContext context) {
        List<String> violations = new ArrayList<>();

        final List<MapValidationContext.MapPosition> portals = context.findMapTiles(TileType.PORTAL.getType());
        final List<MapValidationContext.MapPosition> hiddenPortals = context.findMapTiles(TileType.WALL_HIDING_PORTAL.getType());

        final int portalCount = portals.size() + hiddenPortals.size();
        if (portalCount != 0 && portalCount != 2) {
            violations.add("There must be no or exactly 2 portals on the map");
        }

        portals.forEach(position -> collectAccessibilityViolation(context, position, "Portal", violations));
        hiddenPortals.forEach(position -> collectAccessibilityViolation(context, position, "Hidden portal", violations));

        return violations;
    }

    private void collectAccessibilityViolation(MapValidationContext context,
                                               MapValidationContext.MapPosition position,
                                               String label,
                                               List<String> violations) {
        if (ConstraintUtils.isFullySurroundedByNonWalkableTiles(context, position)) {
            violations.add(label + " at (" + position.column() + "," + position.row() + ") must not be fully surrounded by destructible or other non-walkable tiles");
        }
    }
}
