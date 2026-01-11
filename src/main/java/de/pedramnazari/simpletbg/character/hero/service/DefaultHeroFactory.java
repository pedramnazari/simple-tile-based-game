package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Sorcerer;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;

public class DefaultHeroFactory extends AbstractTileMapElementFactory<IHero> implements IHeroFactory {

    @Override
    protected IHero createNonEmptyElement(int type, int x, int y) {
        return new Sorcerer(x, y);
    }
}
