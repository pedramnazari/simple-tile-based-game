package de.pedramnazari.simpletbg.tilemap.model;

public interface IItem extends ITileMapElement {
    String getName();

    String getDescription();

    @Override
    int getX();

    @Override
    int getY();

    int getType();
}
