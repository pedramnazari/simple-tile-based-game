package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class SwampMapConfig {

    private SwampMapConfig() {
    }

    public static final int HERO_START_ROW = 6;
    public static final int HERO_START_COLUMN = 6;

    private static final int W = WALL.getType();
    private static final int G = GRASS.getType();
    private static final int P = PATH.getType();
    private static final int M = FLOOR2.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int PP = POISON_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int SW = WEAPON_SWORD.getType();
    private static final int RI = RING_MAGIC1.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int EFH = ENEMY_FH.getType();
    private static final int E2D = ENEMY_2D.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, G, G, P, P, G, G, G, M, M, G, G, W},
            {W, G, W, W, P, G, W, G, M, W, G, G, W},
            {W, G, G, G, P, G, W, G, M, W, G, X, W},
            {W, M, M, G, P, G, G, G, M, G, G, G, W},
            {W, M, W, W, P, W, W, G, W, G, W, G, W},
            {W, G, G, G, G, G, P, G, G, G, W, G, W},
            {W, G, W, M, M, G, P, M, M, G, W, G, W},
            {W, G, G, G, W, G, P, G, G, G, G, G, W},
            {W, G, W, G, W, G, G, G, W, W, G, G, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, HP, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, BM, O, O, O, O, O, O, O, O},
            {O, O, O, HP, O, O, O, O, O, O, SW, O, O},
            {O, O, O, O, O, O, O, O, O, HP, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, PP, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, RI, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, E2D, O, O, O, O},
            {O, O, O, O, O, O, O, O, EFH, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, ELR, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, ETD, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, ETD, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
