package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class DesertMapConfig {

    private DesertMapConfig() {
    }

    public static final int HERO_START_ROW = 2;
    public static final int HERO_START_COLUMN = 2;

    private static final int W = WALL.getType();
    private static final int F = FLOOR1.getType();
    private static final int P = PATH.getType();
    private static final int G = GRASS.getType();
    private static final int D = DESTRUCTIBLE_WALL.getType();
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

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, F, F, F, P, P, F, F, F, F, F, D, F, W},
            {W, F, D, F, W, F, F, F, G, F, W, F, F, W},
            {W, F, F, F, F, F, D, F, F, F, F, F, F, W},
            {W, F, F, W, W, F, F, F, F, D, F, W, F, W},
            {W, P, P, F, F, P, P, P, F, F, F, F, F, W},
            {W, F, F, F, D, F, W, F, F, W, F, F, X, W},
            {W, F, W, F, F, F, F, F, F, F, F, W, F, W},
            {W, F, F, F, P, P, F, D, F, F, F, F, F, W},
            {W, F, D, F, F, W, F, F, F, W, F, F, F, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, HP, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, RI, O, O, O, O, O},
            {O, O, O, O, O, O, O, BM, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, FS, O, O, O},
            {O, O, O, O, O, O, O, HP, O, O, O, O, O, O},
            {O, SW, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, HP, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, BM, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, ELR, O, O, O, O, O, O, O, O, E2D, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, ETD, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, ELR, O, O},
            {O, O, O, O, O, O, E2D, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, ETD, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
