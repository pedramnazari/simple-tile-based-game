package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.AbstractTileMapElementFactory;
import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.IHeroFactory;

public class DefaultHeroFactory extends AbstractTileMapElementFactory<Hero> implements IHeroFactory {

    @Override
    protected Hero createNonEmptyElement(int type, int x, int y) {
        return new Hero(x, y);
    }
}
