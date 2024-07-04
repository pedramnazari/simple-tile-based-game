package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.character.model.Character;

public interface IHeroAttackListener {

    void onHeroAttacksCharacter(Character character, int damage);
}
