package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class CitadelMapConfig {

    private CitadelMapConfig() {
    }

    public static final int HERO_START_ROW = 2;
    public static final int HERO_START_COLUMN = 13;

    private static final int W = WALL.getType();
    private static final int P = PATH.getType();
    private static final int G = GRASS.getType();
    private static final int D = DESTRUCTIBLE_WALL.getType();
    private static final int F = FLOOR1.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int RI = RING_MAGIC1.getType();
    private static final int SW = WEAPON_SWORD.getType();
    private static final int FS = WEAPON_FIRE_STAFF.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int E2D = ENEMY_2D.getType();
    private static final int EFH = ENEMY_FH.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, G, G, G, G, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, G, G, G, G, F, W},
            {W, F, G, G, G, G, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, G, G, G, G, F, W},
            {W, F, G, G, D, G, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, G, D, G, G, F, W},
            {W, F, G, G, G, D, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, D, G, G, G, F, W},
            {W, F, F, F, F, F, D, F, W, W, W, W, P, P, P, W, W, W, W, F, D, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, D, W, F, F, F, F, F, F, F, F, F, W, D, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, D, F, F, F, F, F, F, F, F, F, D, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, P, D, F, F, F, F, F, F, F, D, P, F, F, F, F, F, F, F, W},
            {W, P, P, P, P, P, P, P, P, F, D, F, F, F, F, F, D, F, P, P, P, P, P, P, P, P, W},
            {W, F, F, F, F, F, F, F, P, D, F, F, F, F, F, F, F, D, P, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, D, F, F, F, F, F, F, F, F, F, D, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, D, W, F, F, F, F, F, F, F, F, F, W, D, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, D, F, W, W, W, W, P, P, P, W, W, W, W, F, D, F, F, F, F, F, W},
            {W, F, G, G, G, D, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, D, G, G, G, F, W},
            {W, F, G, G, D, G, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, G, D, G, G, F, W},
            {W, F, G, G, G, G, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, G, G, G, G, F, W},
            {W, F, G, G, G, G, F, F, F, F, F, F, F, P, F, F, F, F, F, F, F, G, G, G, G, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, X, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, HP, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, HP, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, HP, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, SW, O, O, O, O, O, O, SW, O, O, O, RI, O, O, SW, O, O, O, O, O, O, SW, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, HP, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, HP, O, O, O},
            {O, O, O, O, O, O, O, O, O, BM, O, O, O, O, FS, O, O, BM, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ETD, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, ETD, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, ELR, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, E2D, O, O, O, O, O, O, O, E2D, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O, O, ELR, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, ETD, O, O, O, O, O, O, O, O, O, O, O, O, O, ETD, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, EFH, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
