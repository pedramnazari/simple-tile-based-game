package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.drivers.ui.view.GameWorldVisualizer;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.inventory.model.ConsumableItem;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.inventory.service.magiceffect.HealthModifierMagicEffect;
import de.pedramnazari.simpletbg.tilemap.adapters.TileConfigParser;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.DefaultTileFactory;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.TileMapService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HeroHitIntegrationTest {

    private static final int FLOOR = TileType.FLOOR1.getType();

    private HeroService heroService;
    private EnemyService enemyService;
    private ItemService itemService;
    private TileMapService tileMapService;
    private GameWorldController controller;
    private TestGameWorldVisualizer visualizer;

    @BeforeEach
    void setUp() {
        GameContext.resetInstance();

        final CollisionDetectionService collisionDetectionService = new CollisionDetectionService();
        final HeroMovementService heroMovementService = new HeroMovementService(collisionDetectionService);
        heroService = new HeroService(new DefaultHeroFactory(), heroMovementService, new HeroAttackService());

        final EnemyMovementService enemyMovementService = new EnemyMovementService(collisionDetectionService);
        enemyService = new EnemyService(enemyMovementService);

        itemService = new ItemService();
        tileMapService = new TileMapService(new DefaultTileFactory());

        final GameWorldService gameWorldService = new GameWorldService(
                tileMapService,
                itemService,
                heroService,
                enemyService);

        controller = new GameWorldController(gameWorldService);
        visualizer = new TestGameWorldVisualizer();
        controller.setTileMapVisualizer(visualizer);
        enemyService.addHeroHitListener(controller);
        heroService.addItemEventListener(itemService);
    }

    @AfterEach
    void tearDown() {
        GameContext.resetInstance();
    }

    @Test
    void heroMovingIntoStationaryEnemyTriggersDamageWhenNotified() {
        final Tile[][] tiles = new TileConfigParser().parse(new int[][]{{FLOOR, FLOOR}}, new DefaultTileFactory());

        heroService.init(0, 0);
        final Enemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 1, 0);
        enemy.setAttackingPower(10);
        enemy.setMovementStrategy(new StayPutStrategy());
        enemyService.init(List.of(enemy));
        tileMapService.initTileMap(tiles);
        GameContext.initialize(tileMapService.getTileMap(), itemService, heroService, enemyService, "0");

        final MovementResult moveResult = heroService.moveHero(MoveDirection.RIGHT, GameContext.getInstance());
        final IHero hero = heroService.getHero();

        assertTrue(moveResult.hasElementMoved());
        assertEquals(1, moveResult.getCollidingElements().size());
        assertTrue(moveResult.getCollidingElements().contains(enemy));
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());
        assertEquals(100, hero.getHealth());

        controller.onHeroHit(hero, enemy, enemy.getAttackingPower());

        assertEquals(90, hero.getHealth());
        assertEquals(1, visualizer.heroHitCount);
    }

    @Test
    void enemyMovingIntoHeroDamagesHero() {
        final Tile[][] tiles = new TileConfigParser().parse(new int[][]{{FLOOR, FLOOR, FLOOR}}, new DefaultTileFactory());

        heroService.init(0, 0);
        final Enemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 2, 0);
        enemy.setAttackingPower(12);
        enemy.setMovementStrategy(new SequenceMovementStrategy(List.of(new Point(1, 0), new Point(0, 0))));
        enemyService.init(List.of(enemy));

        tileMapService.initTileMap(tiles);
        GameContext.initialize(tileMapService.getTileMap(), itemService, heroService, enemyService, "0");

        final IHero hero = heroService.getHero();
        assertEquals(100, hero.getHealth());

        enemyService.moveEnemies(GameContext.getInstance());
        assertEquals(1, enemy.getX());
        assertEquals(0, enemy.getY());
        assertEquals(100, hero.getHealth());
        assertEquals(0, visualizer.heroHitCount);

        enemyService.moveEnemies(GameContext.getInstance());
        assertEquals(0, enemy.getX());
        assertEquals(0, enemy.getY());
        assertEquals(88, hero.getHealth());
        assertEquals(1, visualizer.heroHitCount);
    }

    @Test
    void heroAndEnemyMovingIntoSameTileResultsInDamage() {
        final Tile[][] tiles = new TileConfigParser().parse(new int[][]{{FLOOR, FLOOR, FLOOR}}, new DefaultTileFactory());

        heroService.init(0, 0);
        final Enemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 2, 0);
        enemy.setAttackingPower(17);
        enemy.setMovementStrategy(new SequenceMovementStrategy(List.of(new Point(1, 0))));
        enemyService.init(List.of(enemy));

        tileMapService.initTileMap(tiles);
        GameContext.initialize(tileMapService.getTileMap(), itemService, heroService, enemyService, "0");

        final IHero hero = heroService.getHero();
        final MovementResult moveResult = heroService.moveHero(MoveDirection.RIGHT, GameContext.getInstance());
        assertTrue(moveResult.hasElementMoved());
        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());

        enemyService.moveEnemies(GameContext.getInstance());

        assertEquals(1, enemy.getX());
        assertEquals(0, enemy.getY());
        assertEquals(83, hero.getHealth());
        assertEquals(1, visualizer.heroHitCount);
    }

    @Test
    void heroPickingPoisonConsumableDamagesHero() {
        final Tile[][] tiles = new TileConfigParser().parse(new int[][]{{FLOOR, FLOOR}}, new DefaultTileFactory());

        heroService.init(0, 0);
        enemyService.init(List.of());
        tileMapService.initTileMap(tiles);
        GameContext.initialize(tileMapService.getTileMap(), itemService, heroService, enemyService, "0");

        final ConsumableItem poison = new ConsumableItem(new HealthModifierMagicEffect(-25), 1, 0, TileType.POISON_POTION.getType());
        itemService.addItem(poison);

        final IHero hero = heroService.getHero();
        heroService.moveHero(MoveDirection.RIGHT, GameContext.getInstance());

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());
        assertEquals(75, hero.getHealth());
        assertTrue(itemService.getItem(1, 0).isEmpty());
    }

    private static class StayPutStrategy implements IMovementStrategy {
        @Override
        public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
            return new Point(element.getX(), element.getY());
        }
    }

    private static class SequenceMovementStrategy implements IMovementStrategy {
        private final Deque<Point> moves;

        private SequenceMovementStrategy(Collection<Point> moves) {
            this.moves = new ArrayDeque<>(moves);
        }

        @Override
        public Point calcNextMove(TileMap tileMap, IMovableTileElement element) {
            if (moves.isEmpty()) {
                return new Point(element.getX(), element.getY());
            }

            return moves.pollFirst();
        }
    }

    private static class TestGameWorldVisualizer extends GameWorldVisualizer {
        private int heroHitCount;

        @Override
        public void handleHeroHit() {
            heroHitCount++;
        }

        @Override
        public void handleHeroDefeated() {
            heroHitCount++;
        }
    }
}
