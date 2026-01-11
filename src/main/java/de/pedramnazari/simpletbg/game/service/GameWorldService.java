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
    private static final long MANA_REGEN_INTERVAL_MS = 2000L; // Regenerate mana every 2 seconds
    private static final int MANA_REGEN_AMOUNT = 5; // Regenerate 5 mana per tick

    private final ITileMapService tileMapService;
    private final IItemService itemService;
    private final IHeroService heroService;
    private final IEnemyService enemyService;

    private String currentMapIndex;
    private boolean initialized = false;

    private Quest quest;


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

        // Mana regeneration runner for sorcerer
        Runnable manaRegenRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    IHero hero = heroService.getHero();
                    if (hero instanceof de.pedramnazari.simpletbg.character.hero.model.Sorcerer sorcerer) {
                        if (sorcerer.getMana() < sorcerer.getMaxMana()) {
                            sorcerer.increaseMana(MANA_REGEN_AMOUNT);
                            logger.fine("Sorcerer mana regenerated: " + sorcerer.getMana() + "/" + sorcerer.getMaxMana());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Wait 3 seconds before starting the first move to ensure that game is fully initialized
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.scheduleAtFixedRate(moveEnemiesRunner, ENEMY_MOVE_INITIAL_DELAY_MS, ENEMY_MOVE_INTERVAL_MS, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(manaRegenRunner, ENEMY_MOVE_INITIAL_DELAY_MS, MANA_REGEN_INTERVAL_MS, TimeUnit.MILLISECONDS);
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

    public void onInventarItemSelected(IItem item) {
        heroService.useItem(item);
    }

    public void heroCastFireballDamageEnemies(int damage, int range) {
        IHero hero = heroService.getHero();
        MoveDirection direction = hero.getMoveDirection().orElse(MoveDirection.RIGHT);
        
        int heroX = hero.getX();
        int heroY = hero.getY();
        
        logger.info("Fireball spell targeting enemies in direction: " + direction + " with range: " + range);
        
        // Check each tile in the direction for enemies
        for (int i = 1; i <= range; i++) {
            int targetX = heroX;
            int targetY = heroY;
            
            switch (direction) {
                case UP -> targetY = heroY - i;
                case DOWN -> targetY = heroY + i;
                case LEFT -> targetX = heroX - i;
                case RIGHT -> targetX = heroX + i;
            }
            
            // Check if there's an obstacle (fireball stops at walls)
            TileMap tileMap = getTileMap();
            if (targetX < 0 || targetY < 0 || targetX >= tileMap.getWidth() || targetY >= tileMap.getHeight()) {
                break; // Out of bounds
            }
            
            Tile tile = tileMap.getTile(targetY, targetX);
            if (tile != null && tile.isObstacle()) {
                logger.info("Fireball hit obstacle at (" + targetX + ", " + targetY + ")");
                break; // Stop at obstacle
            }
            
            // Damage enemies at this position
            for (IEnemy enemy : getEnemies()) {
                if (enemy.getX() == targetX && enemy.getY() == targetY) {
                    enemy.decreaseHealth(damage);
                    logger.info("Fireball hit enemy at (" + targetX + ", " + targetY + ") for " + damage + " damage! Enemy health: " + enemy.getHealth());
                }
            }
        }
    }

    public void heroTeleport(int maxDistance) {
        IHero hero = heroService.getHero();
        MoveDirection direction = hero.getMoveDirection().orElse(MoveDirection.RIGHT);
        
        int heroX = hero.getX();
        int heroY = hero.getY();
        
        logger.info("Teleport spell attempting to teleport in direction: " + direction);
        
        // Find the furthest valid position to teleport to
        int teleportX = heroX;
        int teleportY = heroY;
        
        for (int i = 1; i <= maxDistance; i++) {
            int targetX = heroX;
            int targetY = heroY;
            
            switch (direction) {
                case UP -> targetY = heroY - i;
                case DOWN -> targetY = heroY + i;
                case LEFT -> targetX = heroX - i;
                case RIGHT -> targetX = heroX + i;
            }
            
            // Check if target is valid (not obstacle, within bounds)
            TileMap tileMap = getTileMap();
            if (targetX < 0 || targetY < 0 || targetX >= tileMap.getWidth() || targetY >= tileMap.getHeight()) {
                break; // Out of bounds
            }
            
            Tile tile = tileMap.getTile(targetY, targetX);
            if (tile != null && tile.isObstacle()) {
                break; // Can't teleport through walls
            }
            
            // This position is valid
            teleportX = targetX;
            teleportY = targetY;
        }
        
        // Teleport hero to the furthest valid position
        if (teleportX != heroX || teleportY != heroY) {
            hero.setX(teleportX);
            hero.setY(teleportY);
            logger.info("Teleported hero to (" + teleportX + ", " + teleportY + ")");
            
            // The hero position changed, but we don't need to notify here as it's handled differently
        } else {
            logger.info("Teleport failed - no valid destination");
        }
    }
}
