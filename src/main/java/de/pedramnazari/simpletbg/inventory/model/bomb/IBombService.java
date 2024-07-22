package de.pedramnazari.simpletbg.inventory.model.bomb;

import de.pedramnazari.simpletbg.tilemap.model.IBomb;

import java.util.List;

public interface IBombService {
    void placeBomb(IBomb bomb);

    List<IBomb> getBombs();

    void removeBomb(IBomb bomb);
}
