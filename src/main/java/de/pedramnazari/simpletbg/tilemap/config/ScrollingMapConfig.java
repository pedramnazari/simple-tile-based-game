package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class ScrollingMapConfig {

    private ScrollingMapConfig() {
    }

    public static final int HERO_START_COLUMN = 2;
    public static final int HERO_START_ROW = 2;

    private static final int W = WALL.getType();
    private static final int F = FLOOR1.getType();
    private static final int G = GRASS.getType();
    private static final int P = PATH.getType();
    private static final int GS = GRASS_WITH_STONES.getType();
    private static final int D = DESTRUCTIBLE_WALL.getType();
    private static final int PO = PORTAL.getType();
    private static final int X = EXIT.getType();
    private static final int O = EMPTY.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, G, G, G, G, F, F, F, GS, GS, F, F, F, G, G, G, F, F, W},
            {W, F, G, W, W, G, F, W, W, F, F, F, F, W, W, F, F, W, F, W},
            {W, F, G, W, P, G, F, W, F, F, D, F, F, F, F, W, F, F, F, W},
            {W, F, F, F, P, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, W, W, W, F, W, W, F, F, F, F, W, W, F, F, W, F, W},
            {W, F, F, F, PO, W, F, W, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, W, W, W, F, W, W, W, W, F, D, W, W, F, F, W, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, F, W, W, W, W, W, F, F, F, W, W, W, W, W, F, F, W},
            {W, F, F, F, W, GS, GS, F, W, F, F, F, W, F, PO, GS, W, F, F, W},
            {W, F, F, F, W, F, F, F, W, F, F, F, W, F, F, F, W, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, X, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W}
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, WEAPON_SWORD.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, WEAPON_LANCE.getType(), O, O, O, O, O, O, O},
            {O, O, O, O, O, HEALTH_POTION.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, WEAPON_DOUBLE_ENDED_LANCE.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, RING_MAGIC1.getType(), O, O, O},
            {O, O, O, O, O, O, O, O, O, O, WEAPON_MULTI_SPIKE_LANCE.getType(), O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, WEAPON_SWORD2.getType(), O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, POISON_POTION.getType(), O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, HEALTH_POTION.getType(), O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O}
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, ENEMY_LR.getType(), O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, ENEMY_LR.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, ENEMY_TD.getType(), O, O, O},
            {O, O, O, O, O, ENEMY_FH.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, ENEMY_2D.getType(), O, O, O, O, O, O, O, O},
            {O, O, O, ENEMY_LR.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, ENEMY_TD.getType(), O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, ENEMY_TD.getType(), O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, ENEMY_2D.getType(), O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, ENEMY_FH.getType(), O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, ENEMY_LR.getType(), O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, ENEMY_TD.getType(), O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O}
    };
}
