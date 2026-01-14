package de.pedramnazari.simpletbg.tilemap.model;

public interface IArmor extends IItem {
    /**
     * Get the attacking damage of the armor.
     * @return the attacking damage
     */
    int getAttackingDamage();

    /**
     * Set the attacking damage of the armor.
     * @param attackingDamage the attacking damage to set
     */
    void setAttackingDamage(int attackingDamage);

    /**
     * Get the attack range in tiles.
     * @return the attack range
     */
    int getAttackRange();

    /**
     * Set the attack range in tiles.
     * @param attackRange the attack range to set
     */
    void setAttackRange(int attackRange);

    /**
     * Get the cooldown between attacks in milliseconds.
     * @return the cooldown in milliseconds
     */
    long getAttackCooldownMs();

    /**
     * Set the cooldown between attacks in milliseconds.
     * @param cooldownMs the cooldown in milliseconds
     */
    void setAttackCooldownMs(long cooldownMs);

    /**
     * Check if the armor can attack (cooldown has passed).
     * @return true if the armor can attack
     */
    boolean canAttack();

    /**
     * Record that an attack has been performed.
     */
    void recordAttack();
}
