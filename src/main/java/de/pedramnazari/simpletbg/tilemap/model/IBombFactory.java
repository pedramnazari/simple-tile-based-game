package de.pedramnazari.simpletbg.tilemap.model;

public interface IBombFactory {
    IBomb createBomb(int x, int y, int triggerEffectInMilliseconds);
}
