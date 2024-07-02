package de.pedramnazari.simpletbg.game.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.inventory.model.IItemFactory;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemService;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.service.GameContextBuilder;
import de.pedramnazari.simpletbg.tilemap.model.ITileFactory;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameWorldService implements IItemService {

    private static final Logger logger = Logger.getLogger(GameWorldService.class.getName());

    private final HeroService heroService;
    private final EnemyService enemyService;
    private final ITileFactory tileFactory;
    private final IItemFactory itemFactory;

    private final GameContextBuilder gameContextBuilder;

    private String currentMapIndex;
    private boolean initialized = false;

    // TODO: Introduce GameWorld class to hold all maps, items, enemies, hero etc.
    // Maps
    private TileMap tileMap;
    private Collection<Item> items = new ArrayList<>();

    public GameWorldService(ITileFactory tileFactory, IItemFactory itemFactory, HeroService heroService, EnemyService enemyService) {
        this.tileFactory = tileFactory;
        this.itemFactory = itemFactory;
        this.heroService = heroService;
        this.enemyService = enemyService;
        // TODO: inject GameContextBuilder
        this.gameContextBuilder = new GameContextBuilder();
    }

    public TileMap createAndInitMap(final Tile[][] tiles, final Collection<Item> items, Collection<Enemy> enemiesConfig, int heroX, int heroY) {
        if (initialized) {
            throw new IllegalStateException("TileMapService already initialized");
        }

        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        this.items = items;

        this.createAndInitMap(tiles, heroX, heroY);

        initialized = true;

        enemyService.init(enemiesConfig);

        initialized = true;

        return tileMap;
    }

    public TileMap createAndInitMap(final Tile[][] tiles, int heroX, int heroY) {
        // TODO: check whether hero position is valid
        heroService.init(heroX, heroY);

        // TODO: use factory to create map
        this.tileMap = new TileMap("", tiles);

        initialized = true;

        return tileMap;
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
