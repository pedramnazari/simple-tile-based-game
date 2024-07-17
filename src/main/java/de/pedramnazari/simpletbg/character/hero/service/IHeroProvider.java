package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacterProvider;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

// TODO: remove this interface
public interface IHeroProvider extends ICharacterProvider<IHero> {

    IHero getHero();
}
