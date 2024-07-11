package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.model.IHeroFactory;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;

public class DefaultHeroFactory extends AbstractTileMapElementFactory<IHero> implements IHeroFactory {

    @Override
    protected IHero createNonEmptyElement(int type, int x, int y) {
        return new Hero(x, y);
    }
}
