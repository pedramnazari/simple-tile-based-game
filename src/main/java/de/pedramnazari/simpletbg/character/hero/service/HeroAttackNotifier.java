package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;

import java.util.ArrayList;
import java.util.List;

public class HeroAttackNotifier implements IHeroAttackNotifier {
    private final List<IHeroAttackListener> heroAttackListener = new ArrayList<>();

    @Override
    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackListener.add(listener);
    }

    @Override
    public void notifyHeroAttackCharacter(ICharacter attackedCharacter, int damage) {
        for (IHeroAttackListener listener : heroAttackListener) {
            listener.onHeroAttacksCharacter(attackedCharacter, damage);
        }
    }
}
