package de.pedramnazari.simpletbg.tilemap.model;

public interface IBomb extends IWeapon {
    boolean shouldTriggerEffect();

    void triggerEffect();

    boolean isExplosionOngoing();

    boolean isExplosionFinished();
}
