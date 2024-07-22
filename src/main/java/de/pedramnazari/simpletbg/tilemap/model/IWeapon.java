package de.pedramnazari.simpletbg.tilemap.model;

public interface IWeapon extends IItem {
    int getAttackingDamage();

    void setAttackingDamage(int attackingDamage);

    int getRange();

    void setRange(int range);

    /**
     * Check, if the hero can also attack backward.
     * Note: The hero can always attack in the direction he is facing (i.e., forward).
     *
     * @return true, if the hero can attack backward.
     */
    boolean canAttackBackward();

    /**
     * Check, if the hero can attack in all directions.
     * Note: The hero can always attack in the direction he is facing (i.e., forward).
     *
     * @return true, if the hero can attack in all directions.
     */
    boolean canAttackInAllDirections();
}
