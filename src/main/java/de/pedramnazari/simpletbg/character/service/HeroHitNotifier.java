package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IHero;

import java.util.ArrayList;
import java.util.List;

public class HeroHitNotifier {

    private final List<IHeroHitListener> heroHitListener = new ArrayList<>();

    public void addListener(IHeroHitListener listener) {
        heroHitListener.add(listener);
    }

    public void notifyHeroHit(IHero hero, ICharacter attackingCharacter, int damage) {
        for (IHeroHitListener listener : heroHitListener) {
            listener.onHeroHit(hero, attackingCharacter, damage);
        }
    }

}
