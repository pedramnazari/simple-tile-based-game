package de.pedramnazari.simpletbg.tilemap.config;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

/**
 * Elemental Arena - A map designed to showcase the new Ice Wand and Lightning Rod weapons.
 * This map features strategic enemy placement to demonstrate freeze and chain lightning effects.
 */
public final class ElementalArenaMapConfig {

    private ElementalArenaMapConfig() {
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
    private static final int IW = WEAPON_ICE_WAND.getType();      // New Ice Wand
    private static final int LR = WEAPON_LIGHTNING_ROD.getType(); // New Lightning Rod
    private static final int FS = WEAPON_FIRE_STAFF.getType();    // Fire Staff for comparison

    private static final int ELR = ENEMY_LR.getType();
    private static final int ETD = ENEMY_TD.getType();
    private static final int E2D = ENEMY_2D.getType();
    private static final int EFH = ENEMY_FH.getType();

    public static final int[][] MAP = {
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, W, W, W, F, F, F, F, F, W, W, W, F, F, F, W},
            {W, F, F, W, G, W, F, F, F, F, F, W, G, W, F, F, F, W},
            {W, F, F, W, W, W, F, F, D, D, F, W, W, W, F, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W},
            {W, F, F, W, W, W, F, F, F, F, F, W, W, W, F, F, F, W},
            {W, F, F, W, G, W, F, F, F, F, F, W, G, W, F, F, F, W},
            {W, F, F, W, W, W, F, F, D, D, F, W, W, W, F, F, F, W},
            {W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, X, W},
            {W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W},
    };

    public static final int[][] ITEMS = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, IW, O, O, O, O, O, O, O, O, O, O, O, O, LR, O, O}, // Ice Wand (left) and Lightning Rod (right)
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, HP, O, O, O, O, O, O, O, HP, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, FS, O, O, O, O, O, O, O, O, O}, // Fire Staff in the middle
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, HP, O, O, O, O, O, O, O, HP, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };

    public static final int[][] ENEMIES = {
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, ELR, O, O, ELR, O, O, O, O, O, O, O}, // Enemies for freeze testing
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, ETD, O, O, O, O, O, O, O, O, O, ETD, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, E2D, E2D, O, O, E2D, E2D, O, O, O, O, O, O}, // Cluster for chain testing
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, EFH, O, O, EFH, O, O, O, O, O, O, O}, // Follow hero enemies
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
            {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
    };
}
