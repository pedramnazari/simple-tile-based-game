package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public final class SwampMapConfig {

    private SwampMapConfig() {
    }

    public static final int HERO_START_ROW = 1;
    public static final int HERO_START_COLUMN = 1;

    private static final int W = WALL.getType();
    private static final int F = FLOOR1.getType();
    private static final int G = GRASS.getType();
    private static final int D = DESTRUCTIBLE_WALL.getType();
    private static final int X = EXIT.getType();

    private static final int O = EMPTY.getType();
    private static final int HP = HEALTH_POTION.getType();
    private static final int BM = WEAPON_BOMB_PLACER.getType();
    private static final int RI = RING_MAGIC1.getType();
    private static final int SW = WEAPON_SWORD.getType();

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int EFH = ENEMY_FH.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W},
            {W, G, G, F, F, F, F, F, F, G, G, W},
            {W, G, W, F, W, F, W, F, W, F, G, W},
            {W, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, D, F, D, F, D, F, D, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, W},
            {W, G, F, G, F, G, F, G, F, G, F, W},
            {W, F, F, F, F, F, F, F, F, F, X, W},
            {W, F, W, F, W, F, W, F, W, F, F, W},
            {W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, HP, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, BM, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, RI, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, SW, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, ELR, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, ETD, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, EFH, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
