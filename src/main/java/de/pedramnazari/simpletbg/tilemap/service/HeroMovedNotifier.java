package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IHero;

import java.util.ArrayList;
import java.util.List;

public class HeroMovedNotifier {

    private final List<IHeroMovedListener> heroMovedListeners = new ArrayList<>();

    public void addListener(IHeroMovedListener listener) {
        heroMovedListeners.add(listener);
    }

    public void removeListener(IHeroMovedListener listener) {
        heroMovedListeners.remove(listener);
    }

    public void notifyHeroMoved(IHero hero, int oldX, int oldY) {
        for (IHeroMovedListener listener : heroMovedListeners) {
            listener.onHeroMoved(hero, oldX, oldY);
        }
    }
}