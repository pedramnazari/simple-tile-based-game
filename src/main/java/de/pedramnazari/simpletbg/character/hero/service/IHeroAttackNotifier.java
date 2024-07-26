package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;

public interface IHeroAttackNotifier {
    void addHeroAttackListener(IHeroAttackListener listener);

    void notifyHeroAttackCharacter(ICharacter attackedCharacter, int damage);
}
