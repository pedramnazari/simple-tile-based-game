package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;

public interface IHeroAttackListener {

    void onHeroAttacksCharacter(ICharacter character, int damage);
}
