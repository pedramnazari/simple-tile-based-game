package de.pedramnazari.simpletbg.model;

public class DefaultTileFactory implements ITileFactory {

        @Override
        public Tile createTile(int type, int x, int y) {
            final Tile tile = new Tile(type, x, y);

            if (type <= 10) {
                tile.setObstacle(false);
            } else {
                tile.setObstacle(true);
            }

            return tile;
        }
}
