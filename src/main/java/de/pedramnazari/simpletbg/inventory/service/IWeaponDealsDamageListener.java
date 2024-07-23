package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;

public interface IWeaponDealsDamageListener {

    void onWeaponDealsDamage(IWeapon weapon, ICharacter attackedCharacter, int damage);
}
