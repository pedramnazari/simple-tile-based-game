package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.IArmor;

public class Armor extends Item implements IArmor {

    private int attackingDamage = 15;
    private int attackRange = 3;
    private long attackCooldownMs = 1500; // 1.5 seconds between attacks
    private long lastAttackTime = 0;

    public Armor(int x, int y, String name, String description, int type) {
        super(x, y, name, description, type);
    }

    @Override
    public int getAttackingDamage() {
        return attackingDamage;
    }

    @Override
    public void setAttackingDamage(int attackingDamage) {
        this.attackingDamage = attackingDamage;
    }

    @Override
    public int getAttackRange() {
        return attackRange;
    }

    @Override
    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    @Override
    public long getAttackCooldownMs() {
        return attackCooldownMs;
    }

    @Override
    public void setAttackCooldownMs(long cooldownMs) {
        this.attackCooldownMs = cooldownMs;
    }

    @Override
    public boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastAttackTime) >= attackCooldownMs;
    }

    @Override
    public void recordAttack() {
        lastAttackTime = System.currentTimeMillis();
    }
}
