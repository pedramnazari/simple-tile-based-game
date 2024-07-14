package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.TileType;

public class Bomb extends Weapon {
    private final long placedAtTime;
    private final long explodeInMillis;

    public Bomb(int x, int y, String name, String description, long explodeInMillis) {
        super(x, y, name, description, TileType.WEAPON_BOMB.getType());
        this.placedAtTime = System.currentTimeMillis();
        this.explodeInMillis = explodeInMillis;

        setAttackingDamage(20);
    }

    public boolean shouldTriggerEffect() {
        return (System.currentTimeMillis() - placedAtTime) >= explodeInMillis;
    }

}
