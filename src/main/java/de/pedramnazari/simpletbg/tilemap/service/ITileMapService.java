package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

public interface ITileMapService {

    void initTileMap(Tile[][] tiles);

    TileMap getTileMap();
}
