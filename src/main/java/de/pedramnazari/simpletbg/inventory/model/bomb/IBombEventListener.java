package de.pedramnazari.simpletbg.inventory.model.bomb;

import de.pedramnazari.simpletbg.tilemap.model.IBomb;
import de.pedramnazari.simpletbg.tilemap.model.Point;

import java.util.Collection;
import java.util.List;

public interface IBombEventListener {

    void onBombPlaced(IBomb newBomb, Collection<IBomb> allBombs);

    void onBombExploded(IBomb bomb, List<Point> attackPoints);

    void onBombExplosionFinished(IBomb bomb);




}
