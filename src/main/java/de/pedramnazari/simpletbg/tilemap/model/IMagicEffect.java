package de.pedramnazari.simpletbg.tilemap.model;

public interface IMagicEffect {

    String getName();

    void apply(ICharacter character);


    void setAssociatedItem(IItem item);
}
