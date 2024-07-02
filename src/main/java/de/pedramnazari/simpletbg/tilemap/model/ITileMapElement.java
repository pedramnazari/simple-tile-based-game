package de.pedramnazari.simpletbg.tilemap.model;

public interface ITileMapElement {
    // TODO: create enum for tile types
    int EMPTY_TILE_TYPE = 0;


    int getY();

    int getX();

    // TODO: change to TileType getType()
    int getType();
}
