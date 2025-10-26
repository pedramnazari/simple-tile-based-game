package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class GlacierMapConfig {

    private GlacierMapConfig() {
    }

    public static final int HERO_START_ROW = 2;
    public static final int HERO_START_COLUMN = 2;

    private static final int W = WALL.getType();
    private static final int I = FLOOR2.getType();
    private static final int G = GRASS.getType();
    private static final int P = PATH.getType();
    private static final int ST = STONE.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int RI = RING_MAGIC1.getType();
    private static final int SW = WEAPON_SWORD.getType();
    private static final int FS = WEAPON_FIRE_STAFF.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int EFH = ENEMY_FH.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, I, I, I, I, I, ST, ST, I, I, I, I, I, I, I, I, I, W},
            {W, I, G, G, P, I, I, ST, I, G, G, P, I, I, I, G, I, W},
            {W, I, G, W, W, P, I, I, I, I, ST, ST, I, W, W, G, I, W},
            {W, I, P, I, I, P, I, ST, ST, I, I, I, I, I, I, G, I, W},
            {W, I, I, I, ST, ST, I, I, I, I, P, P, I, I, I, I, I, W},
            {W, I, ST, ST, ST, I, I, G, G, I, I, I, ST, ST, I, I, I, W},
            {W, I, I, I, I, I, P, I, I, I, I, I, I, I, I, P, I, W},
            {W, I, G, G, I, I, P, ST, ST, I, G, G, I, I, I, P, I, W},
            {W, I, I, I, I, I, I, I, I, I, I, ST, ST, I, I, I, I, W},
            {W, G, G, I, I, W, W, I, I, W, W, I, I, I, G, G, I, W},
            {W, I, I, I, P, I, I, I, I, I, I, I, P, I, I, I, I, W},
            {W, I, ST, ST, P, I, G, G, I, ST, ST, I, P, I, G, G, X, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, HP, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, RI, O, O, O, O, O, O, O},
            {O, O, BM, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, HP, O, O, O},
            {O, O, O, O, O, O, O, HP, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, SW, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, FS, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, ETD, O},
            {O, O, O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, ELR, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, ETD, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, EFH, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, ETD, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
