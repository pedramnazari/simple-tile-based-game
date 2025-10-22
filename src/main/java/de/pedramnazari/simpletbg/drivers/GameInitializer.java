package de.pedramnazari.simpletbg.drivers;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.adapters.ItemConfigParser;
import de.pedramnazari.simpletbg.inventory.model.BombFactory;
import de.pedramnazari.simpletbg.inventory.model.projectile.FireProjectileFactory;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.inventory.service.bomb.BombService;
import de.pedramnazari.simpletbg.inventory.service.projectile.ProjectileService;
import de.pedramnazari.simpletbg.quest.service.QuestService;
import de.pedramnazari.simpletbg.quest.service.config.DefaultQuestConfigFactory;
import de.pedramnazari.simpletbg.quest.service.config.IQuestConfig;
import de.pedramnazari.simpletbg.quest.service.config.IQuestConfigFactory;
import de.pedramnazari.simpletbg.quest.service.config.QuestDefeatAllEnemiesAndGoToExitConfig;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.config.GameMapDefinition;
import de.pedramnazari.simpletbg.tilemap.config.GameMaps;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.TileMapService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.LeftToRightMovementStrategy;

import java.util.Collection;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public class GameInitializer {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY_LR.getType();
    private static final int E2 = TileType.ENEMY_TD.getType();
    private static final int E3 = TileType.ENEMY_2D.getType();
    private static final int E4 = TileType.ENEMY_FH.getType();

    private static final int W = TileType.WOOD.getType();
    private static final int S = TileType.STONE.getType();
    private static final int GR = TileType.GRASS.getType();
    private static final int WA = TileType.WALL.getType();
    private static final int GS = GRASS_WITH_STONES.getType();
    private static final int F1 = FLOOR1.getType();
    private static final int F2 = FLOOR2.getType();
    private static final int DW = DESTRUCTIBLE_WALL.getType();

    private static final int HP = HEALTH_POTION.getType();
    private static final int PP = POISON_POTION.getType();
    private static final int WHP = WALL_HIDING_PORTAL.getType();


    public static GameWorldController initAndStartGame() {
        return initAndStartGame(GameMaps.defaultMap());
    }

    public static GameWorldController initAndStartGame(GameMapDefinition mapDefinition) {

        // TODO: move to AllTileMapConfigData
        final int[][] mapConfig3 = new int[][]{
                {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11},
                {11, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 11},
                {11, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 11},
                {11, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11, 11, 11, 4, 4, 4, 4, 11},
                {11, 4, 4, 4, 4, 4, 11, 11, 4, 4, 4, 11, 1, 1, 11, 4, 4, 4, 4, 11},
                {11, 4, 4, 4, 11, 11, 4, 11, 11, 4, 4, 11, 1, 1, 11, 4, 4, 4, 4, 11},
                {11, 4, 4, 11, 11, 4, 4, 4, 4, 11, 4, 11, 1, 1, 11, 4, 4, 4, 11, 11},
                {11, 4, 11, 11, 4, 4, 4, 4, 4, 4, 11, 11, 1, 1, 11, 11, 11, 4, 11, 11},
                {11, 11, 11, 4, 4, 4, 11, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11},
                {11, 2, 2, 4, 4, 11, 11, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11},
                {11, 11, 11, 11, 11, 11, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11}
        };

        final int[][] itemConfig3 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, WEAPON_MULTI_SPIKE_LANCE.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, 200, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, WEAPON_BOMB_PLACER.getType(), WEAPON_LANCE.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, RING_MAGIC1.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, 201, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O}
        };

        final int[][] enemyConfig3 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E2, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, E3, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E, O, O, O}
        };

        final int[][] mapConfig4 = new int[][]{
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, WHP},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, WHP, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, DW, 4, WALL_HIDING_EXIT.getType()},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},

        };

        final int[][] itemConfig4 = new int[][]{
                {O, O, WEAPON_MULTI_SPIKE_LANCE.getType(), RING_MAGIC1.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O}
        };

        final int[][] enemyConfig4 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E3},
                {E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O},
                {O, O, O, O, O, O, O, O, O, O, O, E, O, O, O, O, O, O, O, O},
                {E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O},
                {O, O, O, O, O, O, E, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O},
                {O, E, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O, E2, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E4, O, E3},
        };

        final int[][] enemyConfig5 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E3, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E, O, O, O}
        };

        final int[][] mapConfig6 = new int[][]{
                {F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2},
                {F2, WA, WA, WA, WA, WA, WA, WA, WA, DW, DW, WA, WA, WA, WA, WA, WA, WA, WA, F2},
                {F2, WA, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, WA, F2},
                {F2, WA, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, WA, F2},
                {F2, WA, F1, F1, WA, F1, F1, WA, WA, F1, F1, WA, WA, F1, F1, WA, F1, F1, WA, F2},
                {F2, WA, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, WA, F2},
                {F2, WA, F1, F1, F1, WA, WA, F1, F1, WA, WA, F1, F1, WA, WA, F1, F1, F1, WA, F2},
                {F2, WA, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, WA, F2},
                {F2, WA, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, F1, WA, F2},
                {F2, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, WA, F2},
                {F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2, F2}
        };

        final int[][] itemConfig6 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O}
        };

        final int[][] enemyConfig6 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E2},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E2, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, E2, E2, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, E3, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E4}
        };

        final int[][] mapConfig7 = new int[][]{
                {GR, GR, GR, GR, GR, GR, GR, GR, WA, GR, GR, GR, GR, GR, GR, GR, GR, GR, GR, GR},
                {GR, WA, DW, WA, GR, WA, GR, WA, WA, WA, GR, WA, GR, WA, GR, WA, GR, WA, GR, WA},
                {DW, DW, GR, GR, GR, GR, GR, GR, WA, GR, GR, GR, GR, GR, GR, GR, GR, GR, GR, GR},
                {DW, WA, GR, WA, DW, WA, GR, WA, WA, WA, GR, WA, GR, WA, GR, WA, GR, WA, GR, WA},
                {DW, DW, GR, GR, DW, DW, DW, GR, WA, WALL_HIDING_EXIT.getType(), GR, GR, GR, GR, GR, GR, GR, GR, GR, GR},
                {GR, WA, GR, WA, GR, WA, GR, WA, WA, WA, GR, WA, GR, WA, GR, WA, GR, WA, GR, WA},
                {GR, GR, GR, GR, GR, GR, DW, GR, WA, GR, GR, GR, DW, DW, DW, DW, DW, DW, DW, DW},
                {GR, WA, DW, WA, GR, WA, GR, WA, WA, WA, GR, WA, DW, WA, GR, WA, GR, WA, PORTAL.getType(), WA},
                {GR, GR, GR, DW, GR, GR, GR, GR, WA, GR, GR, GR, DW, GR, DW, GR, DW, GR, GR, GR},
                {GR, WA, GR, WA, GR, WA, DW, WA, WA, WA, GR, WA, DW, WA, GR, WA, GR, WA, GR, WA},
                {GR, GR, GR, DW, DW, DW, DW, WHP, WA, GR, GR, GR, DW, DW, DW, DW, DW, DW, DW, DW},
        };

        final int[][] itemConfig7 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, PP, O, O, O, O, O},
                {WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, WEAPON_SWORD2.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, PP, O},
                {PP, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, HP, O, O, O, O, O, O, O, HP, O},
                {O, O, O, O, O, O, O, O, O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O}
        };

        final int[][] enemyConfig7 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E2, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, E2, O, O, O, O, O, O, O, E4, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, E3, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O}
        };

        // TODO: Improve
        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);

        final EnemyService enemyService =
                new EnemyService(enemyMovementService);

        final DefaultItemFactory itemFactory = new DefaultItemFactory();
        final DefaultTileFactory tileFactory = new DefaultTileFactory();
        final TileMapService tileMapService = new TileMapService(tileFactory);
        final ItemService itemService = new ItemService();
        final HeroService heroService = new HeroService(
                new DefaultHeroFactory(),
                new HeroMovementService(collisionDetectionService),
                new HeroAttackService());

        final GameWorldService gameWorldService = new GameWorldService(
                tileMapService,
                itemService,
                heroService,
                enemyService);
        final GameWorldController controller = new GameWorldController(gameWorldService);

        enemyMovementService.addMovementStrategy(new LeftToRightMovementStrategy(collisionDetectionService));
        enemyService.registerObserver(controller);
        enemyService.addItemEventListener(itemService);
        heroService.addItemEventListener(itemService);
        heroService.addHeroAttackListener(enemyService);
        heroService.addHeroMovedListener(tileMapService);
        heroService.addHeroMovedListener(controller);

        heroService.addItemEventListener(controller);
        enemyService.addItemEventListener(controller);
        enemyService.addHeroHitListener(controller);
        enemyService.addEnemyHitListener(controller);

        tileMapService.addTileHitListener(controller);
        tileMapService.addCharacterMovedToSpecialTileListener(heroService);

        /**
         * Bombs
         */
        itemFactory.setBombFactory(new BombFactory());
        BombService bombService = new BombService(heroService, enemyService);
        itemFactory.setBombService(bombService);
        bombService.addBombEventListener(controller);
        bombService.addHeroHitListener(controller);
        bombService.addBombEventListener(tileMapService);
        bombService.addWeaponDealsDamageListener(enemyService);

        ProjectileService projectileService = new ProjectileService(collisionDetectionService, tileMapService, enemyService);
        itemFactory.setProjectileFactory(new FireProjectileFactory());
        itemFactory.setProjectileService(projectileService);
        projectileService.addProjectileEventListener(controller);
        projectileService.addWeaponDealsDamageListener(enemyService);


        /**
         * **** Quests ****
         */
        final IQuestConfigFactory questConfigFactory = new DefaultQuestConfigFactory();
        final IQuestConfig questConfig = questConfigFactory.createQuestConfig(QuestDefeatAllEnemiesAndGoToExitConfig.QUEST_ID);
        final QuestService questService = questConfig.getQuestEventDispatcher();
        enemyService.addEnemyHitListener(questService);
        heroService.addItemEventListener(questService);
        tileMapService.addCharacterMovedToSpecialTileListener(questService);

        gameWorldService.setQuest(questConfig.getQuest());

        final Tile[][] tiles = new TileConfigParser().parse(mapDefinition.getMap(), tileFactory);
        final Collection<IItem> items = new ItemConfigParser().parse(mapDefinition.getItems(), itemFactory);
        final Collection<IEnemy> enemies = new EnemyConfigParser().parse(mapDefinition.getEnemies(), new DefaultEnemyFactory(collisionDetectionService, heroService));

        controller.startGameUsingMap(tiles, items, enemies, mapDefinition.getHeroStartColumn(), mapDefinition.getHeroStartRow());

        return controller;
    }
}
