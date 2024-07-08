package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.service.ICharacterProvider;

public interface IHeroProvider extends ICharacterProvider<Hero> {

    Hero getHero();
}
