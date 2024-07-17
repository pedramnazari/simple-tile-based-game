package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

// TODO: This class represent a dropped bomb that will explode after a certain amount of time.
//       It probably should not extend Weapon (or item). It cannot be picked up etc.
public class Bomb extends Weapon {
    private final long placedAtTime;
    private final long explodeInMillis;
    private long explosionStartTime;
    private boolean hasExploded;
    private final int explosionDurationInMillis = 5000;

    public Bomb(int x, int y, String name, String description, long explodeInMillis) {
        super(x, y, name, description, TileType.WEAPON_BOMB.getType());
        this.placedAtTime = System.currentTimeMillis();
        this.explodeInMillis = explodeInMillis;
        this.hasExploded = false;
        this.explosionStartTime = -1;

        setCanAttackInAllDirections(true);
        setAttackingDamage(10);
    }

    public boolean shouldTriggerEffect() {
        return !hasExploded && ((System.currentTimeMillis() - placedAtTime) >= explodeInMillis);
    }

    public void triggerEffect() {
        if (shouldTriggerEffect()) {
            hasExploded = true;
            explosionStartTime = System.currentTimeMillis();
        }
    }

    public boolean isExplosionOngoing() {
        if (hasExploded) {
            long currentTime = System.currentTimeMillis();
            return (currentTime - explosionStartTime) <= explosionDurationInMillis;
        }
        return false;
    }

    public boolean isExplosionFinished() {
        if (hasExploded) {
            return !isExplosionOngoing();
        }
        return false;
    }

}
