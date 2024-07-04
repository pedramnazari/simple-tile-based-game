package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;

public interface IHeroAttackNotifier {
    void addHeroAttackListener(IHeroAttackListener listener);

    void notifyHeroAttacksCharacter(Character attackedCharacter, int damage);
}
