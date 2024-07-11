package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacterProvider;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

public interface IHeroProvider extends ICharacterProvider<IHero> {

    IHero getHero();
}
