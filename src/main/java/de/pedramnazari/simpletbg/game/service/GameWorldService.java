package de.pedramnazari.simpletbg.game.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.quest.model.Quest;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.service.GameContextBuilder;
import de.pedramnazari.simpletbg.tilemap.model.*;
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

    private final ItemService itemService;
    private final HeroService heroService;
    private final EnemyService enemyService;

    private final GameContextBuilder gameContextBuilder;

    private String currentMapIndex;
    private boolean initialized = false;

    // TODO: Introduce GameWorld class to hold all maps, items, enemies, hero etc.
    // Maps
    private TileMap tileMap;
    private Quest quest;


    public GameWorldService(ItemService itemService, HeroService heroService, EnemyService enemyService) {
        this.itemService = itemService;
        this.heroService = heroService;
        this.enemyService = enemyService;
        // TODO: inject GameContextBuilder
        this.gameContextBuilder = new GameContextBuilder();
    }

    public TileMap createAndInitMap(final Tile[][] tiles, final Collection<IItem> items, Collection<Enemy> enemiesConfig, int heroX, int heroY) {
        if (initialized) {
            throw new IllegalStateException("TileMapService already initialized");
        }

        // TODO: check consistency between tile map and item map (e.g. whether item is on obstacle)
        itemService.addItems(items);

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
                    enemyService.moveEnemies(buildGameContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Wait 3 seconds before starting the first move to ensure that game is fully initialize
        scheduler.scheduleAtFixedRate(moveEnemiesRunner, 3000, 750, TimeUnit.MILLISECONDS);
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
                .setItemService(itemService)
                .setHero(heroService.getHero())
                .setEnemies(enemyService.getEnemies())
                .setCurrentMapIndex(currentMapIndex)
                .build();
    }

    public String getCurrentMapIndex() {
        return currentMapIndex;
    }



    public TileMap getTileMap() {
        return tileMap;
    }

    public IHero getHero() {
        return heroService.getHero();
    }


    public boolean isInitialized() {
        return initialized;
    }

    public Collection<Enemy> getEnemies() {
        return enemyService.getEnemies();
    }



    public HeroService getHeroService() {
        return heroService;
    }

    public EnemyService getEnemyService() {
        return enemyService;
    }

    public ItemService getItemService() {
        return itemService;
    }


    public List<Point> heroAttacks() {
        return heroService.heroAttacks(enemyService.getEnemies());
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
