package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;

import java.util.ArrayList;
import java.util.List;

public class WeaponDealsDamageNotifier  {
    private final List<IWeaponDealsDamageListener> weaponDealsDamageListeners = new ArrayList<>();

    public void addListener(IWeaponDealsDamageListener listener) {
        weaponDealsDamageListeners.add(listener);
    }

    public void notifyWeaponDealsDamage(IWeapon weapon, ICharacter attackedCharacter, int damage) {
        for (IWeaponDealsDamageListener listener : weaponDealsDamageListeners) {
            listener.onWeaponDealsDamage(weapon, attackedCharacter, damage);
        }
    }
}
