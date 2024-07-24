package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.inventory.model.bomb.IBombEventListener;
import de.pedramnazari.simpletbg.tilemap.model.*;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class TileMapService implements ITileMapService, IBombEventListener {

    private static final Logger logger = Logger.getLogger(TileMapService.class.getName());

    private TileHitNotifier tileHitNotifier = new TileHitNotifier();

    private TileMap tileMap;


    @Override
    public void initTileMap(Tile[][] tiles) {
        tileMap = new TileMap("", tiles);
    }

    @Override
    public TileMap getTileMap() {
        return tileMap;
    }

    @Override
    public void onBombPlaced(IBomb newBomb, Collection<IBomb> allBombs) {

    }

    @Override
    public void onBombExploded(IBomb bomb, List<Point> attackPoints) {
        logger.info("Bomb exploded: " + bomb);
        for (Point attackPoint : attackPoints) {
            int x = attackPoint.getX();
            int y = attackPoint.getY();

            if (tileMap.isWithinBounds(x, y)) {
                final Tile tile = tileMap.getTile(x, y);

                if (tile.isDestroyable() && !tile.isDestroyed()) {
                    tile.hit();

                    notifyTileHit(bomb, tile);

                    if (tile.isDestroyed()) {
                        logger.info("Tile destroyed: " + tile);
                    }
                }
            }
        }
    }

    @Override
    public void onBombExplosionFinished(IBomb bomb) {

    }

    public void addTileHitListener(ITileHitListener listener) {
        tileHitNotifier.addListener(listener);
    }

    public void removeTileHitListener(ITileHitListener listener) {
        tileHitNotifier.removeListener(listener);
    }

    public void notifyTileHit(IWeapon weapon, Tile tile) {
        tileHitNotifier.notifyTileHit(weapon, tile);
    }
}
