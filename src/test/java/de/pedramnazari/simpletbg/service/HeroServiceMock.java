package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

public class HeroServiceMock extends HeroService {

    private final IHero hero;

    public HeroServiceMock(IHero hero) {
        super(null, null, null);
        this.hero = hero;
    }

    public HeroServiceMock() {
        this(new Hero(0, 0));
    }

    @Override
    public IHero getHero() {
        return this.hero;
    }
}
