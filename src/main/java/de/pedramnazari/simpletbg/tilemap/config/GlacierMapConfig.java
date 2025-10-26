package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class GlacierMapConfig {

    private GlacierMapConfig() {
    }

    public static final int HERO_START_ROW = 7;
    public static final int HERO_START_COLUMN = 2;

    private static final int W = WALL.getType();
    private static final int I = FLOOR2.getType();
    private static final int S = STONE.getType();
    private static final int D = DESTRUCTIBLE_WALL.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int PP = POISON_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int FS = WEAPON_FIRE_STAFF.getType();
    private static final int RI = RING_MAGIC1.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int EFH = ENEMY_FH.getType();
    private static final int E2D = ENEMY_2D.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, I, I, I, S, S, I, I, I, I, I, S, I, I, W},
            {W, I, W, I, I, S, I, W, W, I, I, S, I, I, W},
            {W, I, W, I, I, I, I, I, I, I, W, I, I, I, W},
            {W, I, I, I, S, S, W, I, S, I, W, I, W, I, W},
            {W, I, W, I, I, I, I, I, S, I, I, I, I, I, W},
            {W, I, W, I, W, W, I, W, I, W, W, I, W, I, W},
            {W, I, I, I, I, I, I, I, I, I, I, I, I, X, W},
            {W, I, S, S, I, I, W, I, W, S, S, I, I, I, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, HP, O, O, O, O, O, O, O, RI, O, O, O, O, O},
            {O, O, O, O, BM, O, O, O, O, O, O, O, O, HP, O},
            {O, O, O, O, O, O, O, O, O, O, O, FS, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, HP, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, PP, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, HP, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, E2D, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, ETD, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O},
            {O, EFH, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, ETD, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, E2D, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
