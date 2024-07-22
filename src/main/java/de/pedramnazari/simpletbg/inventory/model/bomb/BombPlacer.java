package de.pedramnazari.simpletbg.inventory.model.bomb;

import de.pedramnazari.simpletbg.inventory.model.Weapon;
import de.pedramnazari.simpletbg.tilemap.model.IBomb;
import de.pedramnazari.simpletbg.tilemap.model.IBombFactory;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.logging.Logger;

public class BombPlacer extends Weapon {

    private static final Logger logger = Logger.getLogger(BombPlacer.class.getName());

    private final IBombFactory bombFactory;
    private final IBombService bombService;

    private int triggerExplosionInMilliseconds = 3000;

    public BombPlacer(IBombFactory bombFactory, IBombService bombService, int x, int y) {
        super(x, y, BombPlacer.class.getSimpleName(), "BombPlacer", TileType.WEAPON_BOMB_PLACER.getType());
        this.bombFactory = bombFactory;

        this.bombService = bombService;
    }


    public void placeBomb(int x, int y) {
        logger.info("Place bomb at position: " + x + ", " + y);
        final IBomb bomb = bombFactory.createBomb(x, y, triggerExplosionInMilliseconds);
        bomb.setRange(getRange());
        bombService.placeBomb(bomb);
    }

}
