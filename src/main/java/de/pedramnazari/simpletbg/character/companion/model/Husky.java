package de.pedramnazari.simpletbg.character.companion.model;

import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.tilemap.model.ICompanion;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

public class Husky extends Character implements ICompanion {

    private long lastBarkTime = 0;
    private static final long BARK_COOLDOWN_MS = 3000; // 3 seconds

    public Husky(int x, int y) {
        super(TileType.COMPANION_HUSKY.getType(), x, y);
        this.setAttackingPower(8);
    }

    public boolean canBark() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastBarkTime) >= BARK_COOLDOWN_MS;
    }

    public void bark() {
        lastBarkTime = System.currentTimeMillis();
    }
}
