package de.pedramnazari.simpletbg.tilemap.model;

public interface IMovementStrategy {

    Point calcNextMove(final TileMap tileMap, final IMovableTileElement element);
}