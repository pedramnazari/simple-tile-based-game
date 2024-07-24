package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.tilemap.model.Tile;

public interface ITileHitListener {

    void onTileHit(IWeapon weapon, Tile tile);
}
