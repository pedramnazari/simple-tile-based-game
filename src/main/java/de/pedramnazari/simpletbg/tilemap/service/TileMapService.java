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

        initPortals();
    }

    private void initPortals() {
        final Collection<Tile> portals = tileMap.getTilesOfType(TileType.PORTAL);
        final Collection<Tile> portalBehindWalls = tileMap.getTilesOfType(TileType.WALL_HIDING_PORTAL);

        final int portalCount = portals.size() + portalBehindWalls.size();

        if ((portalCount == 1) || (portalCount > 2)) {
            throw new IllegalStateException("There must be no or exactly 2 portals on the map");
        }

        if (portals.size() == 2) {
            final Tile portal1 = portals.stream().findFirst().get();
            final Tile portal2 = portals.stream().skip(1).findFirst().get();

            linkPortals(portal1, portal2);
        }
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
                            transformToNewTileType(tile, x, y);
                        }
                    }
                }
            }
        }
    }

    private void transformToNewTileType(Tile tile, int x, int y) {
        final Tile newTile = tileFactory.createElement(tile.getTransformToNewTileType(), x, y);
        tileMap.setTile(x, y, newTile);

        linkPortalToDestination(newTile);
    }

    private void linkPortalToDestination(Tile tile) {
        if (tile.isPortal()) {
            Tile portalDestination = tile.getPortalDestination().orElse(null);

            if (portalDestination == null) {
                final Collection<Tile> portals = tileMap.getTilesOfType(TileType.PORTAL);

                portalDestination = portals.stream().filter(p -> !p.equals(tile)).findFirst().orElse(null);
            }

            if (portalDestination != null) {
                linkPortals(tile, portalDestination);
            }

            // Note: portalDestination is null if the destination is still hidden (e.g., behind a wall)
        }
    }

    private void linkPortals(Tile portal1, Tile portal2) {
        portal1.setPortalDestination(portal2);
        portal2.setPortalDestination(portal1);
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

        if (tile.isPortal() || tile.getType() == TileType.EXIT.getType()) {
            logger.info("Hero moved to special tile: " + tile);
            characterMovedToSpecialTileNotifier.notifyCharacterMovedToSpecialTile(hero, tile);
        }


    }

    public void addCharacterMovedToSpecialTileListener(ICharacterMovedToSpecialTileListener listener) {
        characterMovedToSpecialTileNotifier.addListener(listener);
    }

    public void removeCharacterMovedToSpecialTileListener(ICharacterMovedToSpecialTileListener listener) {
        characterMovedToSpecialTileNotifier.removeListener(listener);
    }
}
