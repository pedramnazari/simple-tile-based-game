package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.IHeroProvider;

class IHeroProviderMock implements IHeroProvider {
    final Hero hero = new Hero(0, 0);

    @Override
    public Hero getCharacter() {
        return hero;
    }

    @Override
    public Hero getHero() {
        return hero;
    }
}
