package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.tilemap.model.Tile;

import java.util.ArrayList;
import java.util.List;

public class TileHitNotifier {

    private final List<ITileHitListener> tileHitListener = new ArrayList<>();

    public void addListener(ITileHitListener listener) {
        tileHitListener.add(listener);
    }

    public void removeListener(ITileHitListener listener) {
        tileHitListener.remove(listener);
    }

    public void notifyTileHit(IWeapon weapon, Tile tile) {
        for (ITileHitListener listener : tileHitListener) {
            listener.onTileHit(weapon, tile);
        }
    }


}
