package de.pedramnazari.simpletbg.game.service;

import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameWorldService {

    private static final Logger logger = Logger.getLogger(GameWorldService.class.getName());
    private static final long ENEMY_MOVE_INITIAL_DELAY_MS = 3000L;
    private static final long ENEMY_MOVE_INTERVAL_MS = 1000L;

    private final ITileMapService tileMapService;
    private final IItemService itemService;
    private final IHeroService heroService;
    private final IEnemyService enemyService;

    private String currentMapIndex;
    private boolean initialized = false;

    private Quest quest;
    private ScheduledExecutorService scheduler;


    public GameWorldService(ITileMapService tileMapService, IItemService itemService, IHeroService heroService, IEnemyService enemyService) {
        this.tileMapService = tileMapService;
        this.itemService = itemService;
        this.heroService = heroService;
        this.enemyService = enemyService;
    }

    public TileMap createAndInitMap(final Tile[][] tiles, final Collection<IItem> items, Collection<IEnemy> enemiesConfig, int heroX, int heroY) {
        if (initialized) {
            throw new IllegalStateException("TileMapService already initialized");
        }

        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        itemService.addItems(items);

        final TileMap tileMap = this.createAndInitMap(tiles, heroX, heroY);

        enemyService.init(enemiesConfig);

        initialized = true;

        return tileMap;
    }

    public TileMap createAndInitMap(final Tile[][] tiles, int heroX, int heroY) {
        // TODO: check whether hero position is valid
        heroService.init(heroX, heroY);

        tileMapService.initTileMap(tiles);
        final TileMap tileMap = tileMapService.getTileMap();

        initialized = true;

        return tileMap;
    }

    public void start() {
        if (!initialized) {
            throw new IllegalStateException("TileMapService not initialized");
        }

        logger.log(Level.INFO, "Starting the game loop");
        logger.log(Level.INFO, "The quest is '" + quest.getName() + "': " + quest.getDescription());


        Runnable moveEnemiesRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    enemyService.moveEnemies(GameContext.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Wait 3 seconds before starting the first move to ensure that game is fully initialized
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(moveEnemiesRunner, ENEMY_MOVE_INITIAL_DELAY_MS, ENEMY_MOVE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    // TODO: Move all moveHero* methods to HeroService
    public void moveHeroToLeft() {
        moveHero(MoveDirection.LEFT);
    }

    public void moveHeroToRight() {
        moveHero(MoveDirection.RIGHT);
    }

    public void moveHeroUp() {
        moveHero(MoveDirection.UP);
    }

    public void moveHeroDown() {
        moveHero(MoveDirection.DOWN);
    }

    protected void moveHero(MoveDirection moveDirection) {

        final GameContext gameContext = GameContext.getInstance();

        final MovementResult result = heroService.moveHero(moveDirection, gameContext);

        if (result.hasElementMoved() && result.getNewMapIndex() != null) {
            currentMapIndex = result.getNewMapIndex();
        }
    }

    public TileMap getTileMap() {
        return tileMapService.getTileMap();
    }

    public IHero getHero() {
        return heroService.getHero();
    }


    public boolean isInitialized() {
        return initialized;
    }

    public Collection<IEnemy> getEnemies() {
        return enemyService.getEnemies();
    }



    public IHeroService getHeroService() {
        return heroService;
    }

    public IEnemyService getEnemyService() {
        return enemyService;
    }

    public IItemService getItemService() {
        return itemService;
    }


    public List<Point> heroAttacks() {
        return heroService.heroAttacks(getEnemies());
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }

    public void setCurrentMapIndex(String currentMapIndex) {
        this.currentMapIndex = currentMapIndex;
    }

    public void onInventarItemSelected(IItem item) {
        heroService.useItem(item);
    }
}
