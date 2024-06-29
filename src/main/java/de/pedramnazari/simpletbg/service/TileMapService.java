package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TileMapService implements IItemService {

    private static final Logger logger = Logger.getLogger(TileMapService.class.getName());

    private final HeroService heroService;
    private final EnemyService enemyService;
    private final ITileFactory tileFactory;
    private final IItemFactory itemFactory;

    private final GameContextBuilder gameContextBuilder;

    private MapNavigator mapNavigator;
    private String currentMapIndex;
    private boolean initialized = false;

    // TODO: Introduce GameWorld class to hold all maps, items, enemies, hero etc.
    // Maps
    private TileMap tileMap;
    private Collection<Item> items = new ArrayList<>();

    public TileMapService(ITileFactory tileFactory, IItemFactory itemFactory, HeroService heroService, EnemyService enemyService) {
        this.tileFactory = tileFactory;
        this.itemFactory = itemFactory;
        this.heroService = heroService;
        this.enemyService = enemyService;
        // TODO: inject GameContextBuilder
        this.gameContextBuilder = new GameContextBuilder();
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, TileMapConfig itemConfig, TileMapConfig enemiesConfig, int heroX, int heroY) {
        if (initialized) {
            throw new IllegalStateException("TileMapService already initialized");
        }

        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        this.items = itemFactory.createElementsUsingTileMapConfig(itemConfig);
        final TileMap map = this.createAndInitMap(mapConfig, heroX, heroY);
        enemyService.init(enemiesConfig);

        initialized = true;

        return map;
    }

    public void start() {
        if (!initialized) {
            throw new IllegalStateException("TileMapService not initialized");
        }

        logger.log(Level.INFO, "Starting the game loop");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable moveEnemiesRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    enemyService.moveEnemiesRandomlyWithinMap(buildGameContext());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        scheduler.scheduleAtFixedRate(moveEnemiesRunner, 2, 750, TimeUnit.MILLISECONDS);
    }

    public TileMap createAndInitMap(TileMapConfig mapConfig, int heroX, int heroY) {
        Objects.requireNonNull(mapConfig);

        // TODO: check whether hero position is valid
        heroService.init(heroX, heroY);

        // TODO: use factory to create map
        this.tileMap = new TileMap(tileFactory, mapConfig.getMapId(), mapConfig.getMap());

        initialized = true;

        return tileMap;
    }

    public TileMap createAndInitMap(MapNavigator mapNavigator, final String idOfStartingMap, int heroX, int heroY) {
        this.mapNavigator = mapNavigator;
        heroService.init(heroX, heroY);

        this.currentMapIndex = idOfStartingMap;

        this.tileMap = mapNavigator.getMap(idOfStartingMap);

        return tileMap;
    }

    // TODO: Move all moveHero* methods to HeroService?
    public MovementResult moveHeroToLeft() {
        return moveHero(MoveDirection.LEFT);
    }

    public MovementResult moveHeroToRight() {
        return moveHero(MoveDirection.RIGHT);
    }

    public MovementResult moveHeroUp() {
        return moveHero(MoveDirection.UP);
    }

    public MovementResult moveHeroDown() {
        return moveHero(MoveDirection.DOWN);
    }

    protected MovementResult moveHero(MoveDirection moveDirection) {

        final GameContext gameContext = buildGameContext();

        final MovementResult result = heroService.moveHero(moveDirection, gameContext);

        if (result.hasElementMoved()) {
            currentMapIndex = result.getNewMapIndex();
        }

        return result;
    }

    private GameContext buildGameContext() {
        return gameContextBuilder
                .setTileMap(tileMap)
                .setItemService(this)
                .setHero(heroService.getHero())
                .setEnemies(enemyService.getEnemies())
                .setCurrentMapIndex(currentMapIndex)
                .setMapNavigator(mapNavigator)
                .build();
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }

    @Override
    public Collection<Item> getItems() {
        return List.copyOf(items);
    }

    @Override
    public Optional<Item> getItem(final int x, final int y) {
        for (Item item : items) {
            if ((item.getX() == x) && (item.getY() == y)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
    @Override
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Hero getHero() {
        return heroService.getHero();
    }


    public boolean isInitialized() {
        return initialized;
    }

    public Collection<Enemy> getEnemies() {
        return enemyService.getEnemies();
    }

    public HeroMovementService getHeroMovementService() {
        return heroService.getHeroMovementService();
    }

    public EnemyMovementService getEnemyMovementService() {
        return enemyService.getEnemyMovementService();
    }


}
