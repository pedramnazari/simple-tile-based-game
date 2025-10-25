package de.pedramnazari.simpletbg.tilemap.config.validation;

class ElementOverlapConstraint implements MapConstraint {

    @Override
    public void validate(MapValidationContext context) {
        for (int row = 0; row < context.getHeight(); row++) {
            for (int col = 0; col < context.getWidth(); col++) {
                final int mapTile = context.getMapTileType(row, col);
                final int itemType = context.getItemType(row, col);
                final int enemyType = context.getEnemyType(row, col);

                if (!TileTypeClassifier.isEmpty(itemType) && !TileTypeClassifier.isEmpty(enemyType)) {
                    throw new IllegalStateException("Item and enemy overlap at (" + col + "," + row + ")");
                }

                if (!TileTypeClassifier.isEmpty(itemType)) {
                    ensureTileAllowsPlacement("Item", mapTile, row, col);
                }

                if (!TileTypeClassifier.isEmpty(enemyType)) {
                    ensureTileAllowsPlacement("Enemy", mapTile, row, col);
                }
            }
        }
    }

    private void ensureTileAllowsPlacement(String elementLabel, int mapTile, int row, int col) {
        if (TileTypeClassifier.isPortalTile(mapTile) || TileTypeClassifier.isExitTile(mapTile)) {
            throw new IllegalStateException(elementLabel + " at (" + col + "," + row + ") overlaps with a portal or exit");
        }

        if (TileTypeClassifier.isObstacle(mapTile)) {
            throw new IllegalStateException(elementLabel + " at (" + col + "," + row + ") overlaps with an obstacle tile");
        }
    }
}
