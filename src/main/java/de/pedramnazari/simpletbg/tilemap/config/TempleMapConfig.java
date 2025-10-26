package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class TempleMapConfig {

    private TempleMapConfig() {
    }

    public static final int HERO_START_ROW = 5;
    public static final int HERO_START_COLUMN = 3;

    private static final int W = WALL.getType();
    private static final int F = FLOOR1.getType();
    private static final int S = STONE.getType();
    private static final int D = DESTRUCTIBLE_WALL.getType();
    private static final int P = PORTAL.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int SW = WEAPON_SWORD.getType();
    private static final int LA = WEAPON_LANCE.getType();
    private static final int FS = WEAPON_FIRE_STAFF.getType();
    private static final int RI = RING_MAGIC1.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int EFH = ENEMY_FH.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, S, S, W, F, F, F, F, F, F, F, W, S, S, W},
            {W, S, F, F, F, W, W, W, W, W, F, F, F, S, W},
            {W, W, F, W, F, F, F, P, F, F, F, W, F, W, W},
            {W, F, F, F, D, W, F, F, F, W, D, F, F, F, W},
            {W, F, W, F, F, F, F, X, F, F, F, F, W, F, W},
            {W, F, F, F, D, W, F, F, F, W, D, F, F, F, W},
            {W, W, F, W, F, F, F, P, F, F, F, W, F, W, W},
            {W, S, F, F, F, W, W, W, W, W, F, F, F, S, W},
            {W, S, S, W, F, F, F, F, F, F, F, W, S, S, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, HP, O, O, O, O, O, O, O},
            {O, O, SW, O, O, O, O, O, O, O, O, LA, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, FS, O, O, O},
            {O, O, O, O, O, O, BM, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, RI, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, SW, O, O, O, O},
            {O, O, O, O, O, O, O, HP, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, ELR, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, ETD, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, EFH, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
