package de.pedramnazari.simpletbg.inventory.model;

public class BombFactory {

    public Bomb createBomb(int x, int y, int triggerEffectInMilliseconds) {
        return new Bomb(x, y, "Bomb", "Bomb", triggerEffectInMilliseconds );
    }
}
