package de.pedramnazari.simpletbg.config;

import de.pedramnazari.simpletbg.character.enemy.adapters.EnemyConfigParser;
import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.adapters.ItemConfigParser;
import de.pedramnazari.simpletbg.inventory.model.BombFactory;
import de.pedramnazari.simpletbg.inventory.model.BombService;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.quest.service.QuestEventDispatcher;
import de.pedramnazari.simpletbg.quest.service.config.DefaultQuestConfigFactory;
import de.pedramnazari.simpletbg.quest.service.config.IQuestConfig;
import de.pedramnazari.simpletbg.quest.service.config.IQuestConfigFactory;
import de.pedramnazari.simpletbg.quest.service.config.Quest1Config;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IItem;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.LeftToRightMovementStrategy;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;

import java.util.Collection;

import static de.pedramnazari.simpletbg.tilemap.model.TileType.*;

public class GameInitializer {

    private static final int O = TileType.EMPTY.getType();
    private static final int E = TileType.ENEMY_LR.getType();
    private static final int E2 = TileType.ENEMY_TD.getType();
    private static final int E3 = TileType.ENEMY_2D.getType();
    private static final int E4 = TileType.ENEMY_FH.getType();

    public static GameWorldController initAndStartGame() {

        // TODO: move to AllTileMapConfigData
        final int[][] mapConfig = new int[][]{
                {1,  1, 11, 1,  1,  1,  1,  1,  1,  1, 1, 1, 1, 1, 1},
                {1,  1, 1,  1,  1,  1,  11, 11, 11, 11, 1, 1, 1, 1, 1},
                {11, 2, 11, 1,  1,  1,  11, 2,  2,  2, 1, 1, 1, 1, 1},
                {11, 2, 11, 11, 11, 11, 11, 2,  2,  2, 1, 1, 1, 1, 1},
                {2,  2, 2,  2,  2,  2,  2,  2,  2,  2, 1, 1, 1, 1, 1},
                {2,  2, 2,  2,  2,  2,  2,  2,  2,  2, 1, 11, 11, 11, 11},
                {2,  2, 2,  2,  2,  2,  2,  2,  2,  2, 11, 11, 4, 4, 11},
                {2,  2, 2,  2,  2,  2,  2,  2,  2,  2, 4, 4, 4, 4, 11},
                {2,  2, 2,  2,  2,  2,  2,  2,  2,  2, 11, 11, 4, 4, 11},
        };

        final int[][] itemConfig = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, 100, O, 200, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, 101, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, 101, O, O, 101, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, 201, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
        };

        final int[][] enemyConfig = new int[][]{
                {E3, O, O, E2, O, O, O, O, E, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, E, O, O, O, O, O, O, O, E2, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
        };

        final int[][] mapConfig2 = new int[][]{
                {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11},
                {11,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4, 11},
                {11,  4,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  4, 11},
                {11,  4,  2, 11, 11, 11,  2,  2,  2, 11, 11, 11,  2,  4, 11},
                {11,  4,  2, 11,  1, 11,  2,  2,  2, 11,  1, 11,  2,  4, 11},
                {11,  4,  2, 11,  1, 11,  2,  2,  2, 11,  1, 11,  2,  4, 11},
                {11,  4,  2, 11,  1, 11,  2,  2,  2, 11,  1, 11,  2,  4, 11},
                {11,  4,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  4, 11},
                {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11}
        };

        final int[][] itemConfig2 = new int[][]{
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, 100, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, 101, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, 101, O, 101, O, O, O, 201, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
        };

        final int[][] enemyConfig2 = new int[][]{
                {O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O},
                {O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  E4,  O},
                {O,  O,  E3, O,  O,  O,  O,  O,  E2, O,  O,  O,  O,  O,  O},
                {O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O},
                {O,  O,  O,  O,  E2,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O},
                {O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O},
                {O,  O,  E2, O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O},
                {O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  E,  O,  O,  E4,  O},
                {O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O,  O},
        };

        final int[][] mapConfig3 = new int[][]{
                {11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11},
                {11,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,11},
                {11,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,11},
                {11,4,4,4,4,4,4,4,4,4,4,11,11,11,11,4,4,4,4,11},
                {11,4,4,4,4,4,11,11,4,4,4,11,1,1,11,4,4,4,4,11},
                {11,4,4,4,11,11,4,11,11,4,4,11,1,1,11,4,4,4,4,11},
                {11,4,4,11,11,4,4,4,4,11,4,11,1,1,11,4,4,4,11,11},
                {11,4,11,11,4,4,4,4,4,4,11,11,1,1,11,11,11,4,11,11},
                {11,11, 11,4,4,4,11,4,4,4,4,4,4,4,4,4,4,4,11,11},
                {11,2,2,4,4,11,11,4,4,4,4,4,4,4,4,4,4,4,11,11},
                {11,11,11,11,11,11,4,4,4,4,4,4,4,4,4,4,4,4,11,11}
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
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E4},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E2, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, E3, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
                {O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, E, O, O, E4}
        };

        final int[][] mapConfig4 = new int[][]{
                {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},
                {4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11},
                {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},
                {4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11},
                {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},
                {4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11},
                {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},
                {4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11},
                {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},
                {4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11,4,11},
                {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4},
        };

        final int[][] itemConfig4 = new int[][]{
                {O, O, WEAPON_BOMB_PLACER.getType(), O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O, O},
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

        // TODO: Improve
        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);

        final EnemyService enemyService =
                new EnemyService(enemyMovementService);

        final DefaultItemFactory itemFactory = new DefaultItemFactory();
        final DefaultTileFactory tileFactory = new DefaultTileFactory();
        final ItemService itemService = new ItemService();
        final HeroService heroService = new HeroService(
                new DefaultHeroFactory(),
                new HeroMovementService(collisionDetectionService),
                new HeroAttackService());

        final GameWorldService gameWorldService = new GameWorldService(
                itemService,
                heroService,
                enemyService);
        final GameWorldController controller = new GameWorldController(gameWorldService);

        enemyMovementService.addMovementStrategy(new LeftToRightMovementStrategy(collisionDetectionService));
        enemyService.registerObserver(controller);
        enemyService.addItemPickupListener(itemService);
        heroService.addItemPickupListener(itemService);
        heroService.addHeroAttackListener(enemyService);

        heroService.addItemPickupListener(controller);
        enemyService.addItemPickupListener(controller);
        enemyService.addHeroHitListener(controller);
        enemyService.addEnemyHitListener(controller);

        /**
         * Bombs
         */
        itemFactory.setBombFactory(new BombFactory());
        itemFactory.setBombService(new BombService(heroService, enemyService, controller));



        /**
         * **** Quests ****
         */
        final IQuestConfigFactory questConfigFactory = new DefaultQuestConfigFactory();
        final IQuestConfig quest1Config = questConfigFactory.createQuestConfig(Quest1Config.QUEST_ID);
        final QuestEventDispatcher questEventDispatcher = quest1Config.getQuestEventDispatcher();
        enemyService.addEnemyHitListener(questEventDispatcher);
        heroService.addItemPickupListener(questEventDispatcher);

        gameWorldService.setQuest(quest1Config.getQuest());

        final Tile[][] tiles = new TileConfigParser().parse(mapConfig4, tileFactory);
        final Collection<IItem> items = new ItemConfigParser().parse(itemConfig4, itemFactory);
        final Collection<IEnemy> enemies = new EnemyConfigParser().parse(enemyConfig5, new DefaultEnemyFactory(collisionDetectionService, heroService));

        controller.startGameUsingMap(tiles, items, enemies, 1, 1);

        return controller;
    }
}
