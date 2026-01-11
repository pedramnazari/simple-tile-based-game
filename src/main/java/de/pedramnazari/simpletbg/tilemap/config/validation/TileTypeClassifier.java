package de.pedramnazari.simpletbg.tilemap.config.validation;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

final class TileTypeClassifier {

    private TileTypeClassifier() {
    }

    static boolean isEmpty(int tileType) {
        return tileType == TileType.EMPTY.getType();
    }

    static boolean isPortal(int tileType) {
        return tileType == TileType.PORTAL.getType();
    }

    static boolean isExit(int tileType) {
        return tileType == TileType.EXIT.getType();
    }

    static boolean isExitTile(int tileType) {
        return isExit(tileType) || tileType == TileType.WALL_HIDING_EXIT.getType();
    }

    static boolean isPortalTile(int tileType) {
        return isPortal(tileType) || tileType == TileType.WALL_HIDING_PORTAL.getType();
    }

    static boolean isDestructibleWall(int tileType) {
        return tileType == TileType.DESTRUCTIBLE_WALL.getType()
                || tileType == TileType.WALL_HIDING_PORTAL.getType()
                || tileType == TileType.WALL_HIDING_EXIT.getType();
    }

    static boolean isObstacle(int tileType) {
        if (isPortal(tileType) || isExit(tileType)) {
            return false;
        }
        // Magic tiles are special walkable tiles
        if (tileType == TileType.MAGIC_CRYSTAL.getType() 
            || tileType == TileType.MAGIC_RUNE_STONE.getType()
            || tileType == TileType.MAGIC_SPELL_ALTAR.getType()
            || tileType == TileType.MAGIC_MANA_FOUNTAIN.getType()
            || tileType == TileType.MAGIC_BARRIER.getType()) {
            return false;
        }
        return tileType >= TileType.WALL.getType();
    }

    static boolean isWalkable(int tileType) {
        return !isObstacle(tileType);
    }
}
