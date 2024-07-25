package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.Tile;

public interface ICharacterMovedToSpecialTileListener {

    void onCharacterMovedToSpecialTile(ICharacter character, Tile specialTile);
}
