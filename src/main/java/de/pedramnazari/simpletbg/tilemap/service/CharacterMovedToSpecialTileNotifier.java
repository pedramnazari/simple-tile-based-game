package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.Tile;

import java.util.ArrayList;
import java.util.List;

public class CharacterMovedToSpecialTileNotifier {

    private final List<ICharacterMovedToSpecialTileListener> specialTileListeners = new ArrayList<>();

    public void addListener(ICharacterMovedToSpecialTileListener listener) {
        specialTileListeners.add(listener);
    }

    public void removeListener(ICharacterMovedToSpecialTileListener listener) {
        specialTileListeners.remove(listener);
    }

    public void notifyCharacterMovedToSpecialTile(ICharacter character, Tile specialTile) {
        for (ICharacterMovedToSpecialTileListener listener : specialTileListeners) {
            listener.onCharacterMovedToSpecialTile(character, specialTile);
        }
    }
}