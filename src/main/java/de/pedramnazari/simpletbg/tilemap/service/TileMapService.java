package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.inventory.model.bomb.IBombEventListener;
import de.pedramnazari.simpletbg.tilemap.model.*;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class TileMapService implements ITileMapService, IBombEventListener, IHeroMovedListener {

    private static final Logger logger = Logger.getLogger(TileMapService.class.getName());

    private final ITileFactory tileFactory;
    private final TileHitNotifier tileHitNotifier = new TileHitNotifier();
    private final CharacterMovedToSpecialTileNotifier characterMovedToSpecialTileNotifier = new CharacterMovedToSpecialTileNotifier();

    private TileMap tileMap;

    public TileMapService(ITileFactory tileFactory) {
        this.tileFactory = tileFactory;
    }


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

                if (tile.isDestructible() && !tile.isDestroyed()) {
                    tile.hit();

                    notifyTileHit(bomb, tile);

                    if (tile.isDestroyed()) {
                        if (tile.canTransformToNewTileType()) {
                            tileMap.setTile(x, y, tileFactory.createElement(tile.getTransformToNewTileType(), x, y));
                        }
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

    @Override
    public void onHeroMoved(IHero hero, int oldX, int oldY) {
        final Tile tile = tileMap.getTile(hero.getX(), hero.getY());

        characterMovedToSpecialTileNotifier.notifyCharacterMovedToSpecialTile(hero, tile);

        logger.info("Hero moved to tile: " + tile);
    }

    public void addCharacterMovedToSpecialTileListener(ICharacterMovedToSpecialTileListener listener) {
        characterMovedToSpecialTileNotifier.addListener(listener);
    }

    public void removeCharacterMovedToSpecialTileListener(ICharacterMovedToSpecialTileListener listener) {
        characterMovedToSpecialTileNotifier.removeListener(listener);
    }
}
