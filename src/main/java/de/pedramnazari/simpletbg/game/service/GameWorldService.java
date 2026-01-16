package de.pedramnazari.simpletbg.game.service;

import de.pedramnazari.simpletbg.character.companion.model.Husky;
import de.pedramnazari.simpletbg.inventory.service.ArmorService;
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
    private static final long RUSH_CREATURE_MOVE_INTERVAL_MS = 333L; // Rush creatures move ~3x faster than regular enemies
    private static final long ARMOR_AUTO_ATTACK_INTERVAL_MS = 500L; // Check for armor auto-attacks every 500ms

    private final ITileMapService tileMapService;
    private final IItemService itemService;
    private final IHeroService heroService;
    private final IEnemyService enemyService;
    private final ICompanionService companionService;
    private final ArmorService armorService;

    private String currentMapIndex;
    private boolean initialized = false;
    private boolean running = false;
    private boolean gameEnded = false;
    private ScheduledExecutorService scheduler;

    private Quest quest;


    public GameWorldService(ITileMapService tileMapService, IItemService itemService, IHeroService heroService, IEnemyService enemyService, ICompanionService companionService, ArmorService armorService) {
        this.tileMapService = tileMapService;
        this.itemService = itemService;
        this.heroService = heroService;
        this.enemyService = enemyService;
        this.companionService = companionService;
        this.armorService = armorService;
    }

    public TileMap createAndInitMap(final Tile[][] tiles, final Collection<IItem> items, Collection<IEnemy> enemiesConfig, int heroX, int heroY) {
        if (initialized) {
            throw new IllegalStateException("TileMapService already initialized");
        }

        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        itemService.addItems(items);

        final TileMap tileMap = this.createAndInitMap(tiles, heroX, heroY);

        enemyService.init(enemiesConfig);
        
        // Initialize companion (husky) near the hero
        Husky husky = new Husky(heroX + 1, heroY);
        companionService.init(husky);

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

        if (running) {
            logger.log(Level.WARNING, "Game is already running");
            return;
        }

        logger.log(Level.INFO, "Starting the game loop");
        logger.log(Level.INFO, "The quest is '" + quest.getName() + "': " + quest.getDescription());

        running = true;
        gameEnded = false;

        Runnable moveEnemiesRunner = new Runnable() {
            @Override
            public void run() {
                if (!running || gameEnded) return;
                try {
                    enemyService.moveEnemies(GameContext.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Wait 3 seconds before starting the first move to ensure that game is fully initialized
        scheduler = Executors.newScheduledThreadPool(3); // Pool size 3: enemies, rush creatures, and armor
        scheduler.scheduleAtFixedRate(moveEnemiesRunner, ENEMY_MOVE_INITIAL_DELAY_MS, ENEMY_MOVE_INTERVAL_MS, TimeUnit.MILLISECONDS);
        
        // Separate faster scheduler for rush creatures to keep them constantly moving
        Runnable moveRushCreaturesRunner = new Runnable() {
            @Override
            public void run() {
                if (!running || gameEnded) return;
                try {
                    enemyService.moveRushCreatures(GameContext.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scheduler.scheduleAtFixedRate(moveRushCreaturesRunner, ENEMY_MOVE_INITIAL_DELAY_MS, RUSH_CREATURE_MOVE_INTERVAL_MS, TimeUnit.MILLISECONDS);
        
        // Armor auto-attack scheduler
        Runnable armorAutoAttackRunner = new Runnable() {
            @Override
            public void run() {
                if (!running || gameEnded) return;
                try {
                    armorService.performAutoAttacks(heroService.getHero(), GameContext.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scheduler.scheduleAtFixedRate(armorAutoAttackRunner, ENEMY_MOVE_INITIAL_DELAY_MS, ARMOR_AUTO_ATTACK_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops all game loops and background threads.
     */
    public void stop() {
        logger.log(Level.INFO, "Stopping the game");
        running = false;
        
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    logger.log(Level.WARNING, "Scheduler did not terminate in time");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            scheduler = null;
        }
    }

    /**
     * Marks the game as ended (hero died or quest completed).
     * This stops all game activity without shutting down entirely.
     */
    public void setGameEnded(boolean ended) {
        this.gameEnded = ended;
        if (ended) {
            logger.log(Level.INFO, "Game has ended");
        }
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isRunning() {
        return running;
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

        if (result.hasElementMoved()) {
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
    
    public Collection<ICompanion> getCompanions() {
        return companionService.getCompanions();
    }
    
    public ICompanionService getCompanionService() {
        return companionService;
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

    public void onInventarItemSelected(IItem item) {
        heroService.useItem(item);
    }
}
