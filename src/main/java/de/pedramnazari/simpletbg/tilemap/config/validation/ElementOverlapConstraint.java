package de.pedramnazari.simpletbg.tilemap.config.validation;

import java.util.ArrayList;
import java.util.List;

class ElementOverlapConstraint implements MapConstraint {

    @Override
    public List<String> validate(MapValidationContext context) {
        List<String> violations = new ArrayList<>();

        for (int row = 0; row < context.getHeight(); row++) {
            for (int col = 0; col < context.getWidth(); col++) {
                final int mapTile = context.getMapTileType(row, col);
                final int itemType = context.getItemType(row, col);
                final int enemyType = context.getEnemyType(row, col);

                if (!TileTypeClassifier.isEmpty(itemType) && !TileTypeClassifier.isEmpty(enemyType)) {
                    violations.add("Item and enemy overlap at (" + col + "," + row + ")");
                }

                if (!TileTypeClassifier.isEmpty(itemType)) {
                    ensureTileAllowsPlacement(violations, "Item", mapTile, row, col);
                }

                if (!TileTypeClassifier.isEmpty(enemyType)) {
                    ensureTileAllowsPlacement(violations, "Enemy", mapTile, row, col);
                }
            }
        }

        return violations;
    }

    private void ensureTileAllowsPlacement(List<String> violations, String elementLabel, int mapTile, int row, int col) {
        if (TileTypeClassifier.isPortalTile(mapTile) || TileTypeClassifier.isExitTile(mapTile)) {
            violations.add(elementLabel + " at (" + col + "," + row + ") overlaps with a portal or exit");
        }

        if (TileTypeClassifier.isObstacle(mapTile)) {
            violations.add(elementLabel + " at (" + col + "," + row + ") overlaps with an obstacle tile");
        }
    }
}
