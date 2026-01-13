package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface IProjectile extends IMovableTileElement {
    int getRemainingRange();

    void setRemainingRange(int remainingRange);

    boolean isActive();

    void deactivate();

    IWeapon getWeapon();

    int getDamage();

    /**
     * Get the on-hit effect for this projectile, if any.
     * @return Optional containing the effect, or empty if no effect
     */
    default Optional<IProjectileEffect> getEffect() {
        return Optional.empty();
    }
}
