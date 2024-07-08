package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.character.model.Character;

public interface ICharacterProvider<T extends Character> {

    T getCharacter();
}
