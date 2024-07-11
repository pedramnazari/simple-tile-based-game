package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.IHeroProvider;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

class IHeroProviderMock implements IHeroProvider {
    final IHero hero = new Hero(0, 0);

    @Override
    public IHero getCharacter() {
        return hero;
    }

    @Override
    public IHero getHero() {
        return hero;
    }
}
