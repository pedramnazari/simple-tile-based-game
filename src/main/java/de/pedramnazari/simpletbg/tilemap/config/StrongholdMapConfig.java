package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class StrongholdMapConfig {

    private StrongholdMapConfig() {
    }

    public static final int HERO_START_ROW = 2;
    public static final int HERO_START_COLUMN = 2;

    private static final int W = WALL.getType();
    private static final int F = FLOOR1.getType();
    private static final int S = STONE.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int SW = WEAPON_SWORD.getType();
    private static final int LA = WEAPON_LANCE.getType();
    private static final int DL = WEAPON_DOUBLE_ENDED_LANCE.getType();
    private static final int FS = WEAPON_FIRE_STAFF.getType();
    private static final int RI = RING_MAGIC1.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int EFH = ENEMY_FH.getType();
    private static final int E2D = ENEMY_2D.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, S, S, W, F, F, F, W, F, F, F, W, F, F, F, W, S, W},
            {W, F, F, F, F, W, F, F, F, W, F, F, F, W, F, F, F, W},
            {W, F, W, W, F, W, F, W, F, W, F, W, F, W, F, W, F, W},
            {W, F, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, W, F, W, W, W, F, W, W, W, F, W, W, W, F, W, W},
            {W, F, F, F, W, F, F, F, F, F, W, F, F, F, F, F, F, W},
            {W, W, W, F, W, F, W, W, W, F, W, W, W, F, W, W, W, W},
            {W, F, F, F, F, F, F, F, W, F, F, F, F, F, F, F, F, W},
            {W, F, W, W, W, W, W, F, W, F, W, W, W, W, W, F, W, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, X, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, HP, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, SW, O, O, O, O, O, O, O, O, HP, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, BM, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, RI, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, DL, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, LA, O, O, O, FS, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, ETD, O, O, O, O, O, O, O, ETD, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, E2D, O, EFH, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
