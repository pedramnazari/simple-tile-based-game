package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.inventory.model.bomb.Bomb;
import de.pedramnazari.simpletbg.tilemap.model.IBombFactory;

public class BombFactory implements IBombFactory {

    @Override
    public Bomb createBomb(int x, int y, int triggerEffectInMilliseconds) {
        return new Bomb(x, y, triggerEffectInMilliseconds );
    }
}
