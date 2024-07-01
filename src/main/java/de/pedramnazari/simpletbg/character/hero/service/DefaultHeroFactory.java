package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.model.IHeroFactory;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;

public class DefaultHeroFactory extends AbstractTileMapElementFactory<Hero> implements IHeroFactory {

    @Override
    protected Hero createNonEmptyElement(int type, int x, int y) {
        return new Hero(x, y);
    }
}
