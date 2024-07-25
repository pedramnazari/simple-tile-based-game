package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IHero;

public interface IHeroMovedListener {
    void onHeroMoved(IHero hero, int oldX, int oldY);
}
