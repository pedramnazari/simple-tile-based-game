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
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.quest.model.QuestObjective;
import de.pedramnazari.simpletbg.quest.service.AllEnemiesDefeatedQuestObjective;
import de.pedramnazari.simpletbg.quest.service.ItemPickUpQuestObjective;
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

        final Quest quest = initializeQuest();

        gameWorldService.setQuest(quest);

        enemyMovementService.addMovementStrategy(new LeftToRightMovementStrategy(collisionDetectionService));
        enemyService.registerObserver(controller);
        enemyService.addItemPickupListener(itemService);
        heroService.addItemPickupListener(itemService);
        heroService.addHeroAttackListener(enemyService);

        heroService.addItemPickupListener(controller);
        enemyService.addItemPickupListener(controller);
        enemyService.addHeroHitListener(controller);
        enemyService.addEnemyHitListener(controller);

        enemyService.addEnemyHitListener(quest);
        heroService.addItemPickupListener(quest);


        final Tile[][] tiles = new TileConfigParser().parse(mapConfig2, tileFactory);
        final Collection<IItem> items = new ItemConfigParser().parse(itemConfig2, itemFactory);
        final Collection<IEnemy> enemies = new EnemyConfigParser().parse(enemyConfig2, new DefaultEnemyFactory(collisionDetectionService, heroService));

        controller.startGameUsingMap(tiles, items, enemies, 1, 1);

        return controller;
    }

    private static Quest initializeQuest() {
        final Quest quest = new Quest("Defeat all enemies and collect black sword.", "You have to defeat all enemies and collect the black sword to win the game");
        final QuestObjective questObjective1 = new AllEnemiesDefeatedQuestObjective("Defeat all enemies");
        quest.addObjective(questObjective1);

        final QuestObjective questObjective2 = new ItemPickUpQuestObjective("Collect black sword");
        quest.addObjective(questObjective2);
        return quest;
    }
}
