package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

public interface IHeroHitListener {

    void onHeroHit(IHero hero, ICharacter attackingCharacter, int damage);
}
