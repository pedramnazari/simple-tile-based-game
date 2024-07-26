package de.pedramnazari.simpletbg.inventory.model.bomb;

import de.pedramnazari.simpletbg.inventory.model.Weapon;
import de.pedramnazari.simpletbg.tilemap.model.IBomb;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

// TODO: This class represent a dropped bomb that will explode after a certain amount of time.
//       It probably should not extend Weapon (or item). It cannot be picked up etc.
public class Bomb extends Weapon implements IBomb {
    private static int nextId = 0;


    private final int id;
    private final long placedAtTime;
    private final long explodeInMillis;
    private long explosionStartTime;
    private boolean hasExploded;
    private final int explosionDurationInMillis = 1000;

    public Bomb(int x, int y, long explodeInMillis) {
        super(x, y, "Bomb", "Bomb", TileType.WEAPON_BOMB.getType());
        this.placedAtTime = System.currentTimeMillis();
        this.explodeInMillis = explodeInMillis;
        this.hasExploded = false;
        this.explosionStartTime = -1;

        id = nextId++;

        setCanAttackInAllDirections(true);
        setAttackingDamage(10);
    }

    @Override
    public boolean shouldTriggerEffect() {
        return !hasExploded && ((System.currentTimeMillis() - placedAtTime) >= explodeInMillis);
    }

    @Override
    public void triggerEffect() {
        if (shouldTriggerEffect()) {
            hasExploded = true;
            explosionStartTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean isExplosionOngoing() {
        if (hasExploded) {
            long currentTime = System.currentTimeMillis();
            return (currentTime - explosionStartTime) <= explosionDurationInMillis;
        }
        return false;
    }

    @Override
    public boolean isExplosionFinished() {
        if (hasExploded) {
            return !isExplosionOngoing();
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Bomb other) {
            return other.id == this.id;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Bomb{" + "id=" + id + '}';
    }
}
