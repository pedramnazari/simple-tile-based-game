package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;

public interface IHeroHitListener {

    void onHeroHit(IHero hero, ICharacter attackingCharacter, int damage);

    void onHeroHit(IHero hero, IWeapon weapon, int damage);
}
