package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;

import java.util.ArrayList;
import java.util.List;

public class HeroHitNotifier {

    private final List<IHeroHitListener> heroHitListener = new ArrayList<>();

    public void addListener(IHeroHitListener listener) {
        heroHitListener.add(listener);
    }

    public void removeListener(IHeroHitListener listener) {
        heroHitListener.remove(listener);
    }

    public void notifyHeroHit(IHero hero, ICharacter attackingCharacter, int damage) {
        for (IHeroHitListener listener : heroHitListener) {
            listener.onHeroHit(hero, attackingCharacter, damage);
        }
    }

    public void notifyHeroHit(IHero hero, IWeapon weapon, int damage) {
        for (IHeroHitListener listener : heroHitListener) {
            listener.onHeroHit(hero, weapon, damage);
        }
    }


}
