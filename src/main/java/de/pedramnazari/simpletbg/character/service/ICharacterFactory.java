package de.pedramnazari.simpletbg.character.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.ITileMapElementFactory;

public interface ICharacterFactory<T extends ICharacter> extends ITileMapElementFactory<T> {
}
