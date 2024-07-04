package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.model.Character;

public interface IHeroHitListener {

    void onHeroHit(Hero hero, Character attackingCharacter, int damage);
}
