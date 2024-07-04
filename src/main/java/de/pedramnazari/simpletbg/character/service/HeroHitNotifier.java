package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.model.Character;

import java.util.ArrayList;
import java.util.List;

public class HeroHitNotifier {

    private final List<IHeroHitListener> heroHitListener = new ArrayList<>();

    public void addListener(IHeroHitListener listener) {
        heroHitListener.add(listener);
    }

    public void notifyHeroHit(Hero hero, Character attackingCharacter, int damage) {
        for (IHeroHitListener listener : heroHitListener) {
            listener.onHeroHit(hero, attackingCharacter, damage);
        }
    }

}
