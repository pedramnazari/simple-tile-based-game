package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.logging.Logger;

public class BombPlacer extends Weapon {

    private static final Logger logger = Logger.getLogger(BombPlacer.class.getName());

    private final BombFactory bombFactory;
    private final BombService bombService;

    private int triggerExplosionInMilliseconds = 3000;

    public BombPlacer(BombFactory bombFactory, BombService bombService, int x, int y) {
        super(x, y, BombPlacer.class.getSimpleName(), "BombPlacer", TileType.WEAPON_BOMB_PLACER.getType());
        this.bombFactory = bombFactory;

        this.bombService = bombService;

        setCanAttackInAllDirections(true);
    }


    public void placeBomb(int x, int y) {
        logger.info("Place bomb at position: " + x + ", " + y);
        final Bomb bomb = bombFactory.createBomb(x, y, triggerExplosionInMilliseconds);
        bomb.setRange(getRange());
        bombService.placeBomb(bomb);
    }








}
