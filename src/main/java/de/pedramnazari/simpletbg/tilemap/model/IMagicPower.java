package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Collection;

public interface IMagicPower {

    String getName();

    void activate(IHero hero, Collection<IEnemy> enemies);

    void setAssociatedItem(IItem item);
}
