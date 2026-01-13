package de.pedramnazari.simpletbg.character.companion.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.projectile.WindProjectile;
import de.pedramnazari.simpletbg.character.companion.model.Husky;
import de.pedramnazari.simpletbg.inventory.service.projectile.IProjectileService;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.ICompanionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class CompanionService implements ICompanionService, Runnable {
    private static final Logger logger = Logger.getLogger(CompanionService.class.getName());
    private static final int MAX_DISTANCE_FROM_HERO = 3;
    private static final int MELEE_ATTACK_RANGE = 1;
    private static final int PROJECTILE_RANGE = 5;
    private static final int PROJECTILE_DAMAGE = 5;
    
    private final List<ICompanion> companions = new ArrayList<>();
    private final CollisionDetectionService collisionDetectionService;
    private final IProjectileService projectileService;
    private final Random random = new Random();
    private boolean initialized = false;
    private volatile boolean running = false;

    public CompanionService(CollisionDetectionService collisionDetectionService,
                           IProjectileService projectileService) {
        this.collisionDetectionService = collisionDetectionService;
        this.projectileService = projectileService;
    }

    @Override
    public void init(ICompanion companion) {
        if (initialized) {
            throw new IllegalStateException("Companion service already initialized");
        }
        companions.add(companion);
        initialized = true;
        startService();
    }

    private void startService() {
        Thread companionThread = new Thread(this);
        companionThread.setDaemon(true);
        running = true;
        companionThread.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            GameContext gameContext = GameContext.getInstance();
            if (gameContext != null) {
                updateCompanions(gameContext);
            }
        }
    }

    @Override
    public void updateCompanions(GameContext gameContext) {
        if (companions.isEmpty()) {
            return;
        }

        for (ICompanion companion : companions) {
            if (!(companion instanceof Husky husky)) {
                continue;
            }

            IHero hero = gameContext.getHero();
            Collection<IEnemy> enemies = gameContext.getEnemyService().getEnemies();
            TileMap tileMap = gameContext.getTileMap();

            // Find nearest enemy
            IEnemy nearestEnemy = findNearestEnemy(husky, enemies);

            if (nearestEnemy != null) {
                int distanceToEnemy = calculateDistance(husky.getX(), husky.getY(), 
                                                       nearestEnemy.getX(), nearestEnemy.getY());

                // Melee attack if enemy is adjacent
                if (distanceToEnemy <= MELEE_ATTACK_RANGE) {
                    performMeleeAttack(husky, nearestEnemy);
                } 
                // Ranged bark attack at random intervals
                else if (husky.canBark() && random.nextDouble() < 0.3) {
                    performBarkAttack(husky, nearestEnemy);
                }
            }

            // Move towards hero if too far away, or towards nearest enemy if close
            moveCompanion(husky, hero, nearestEnemy, tileMap);
        }
    }

    private void moveCompanion(Husky husky, IHero hero, IEnemy nearestEnemy, TileMap tileMap) {
        int distanceToHero = calculateDistance(husky.getX(), husky.getY(), hero.getX(), hero.getY());

        int targetX, targetY;
        
        if (nearestEnemy != null && distanceToHero <= MAX_DISTANCE_FROM_HERO) {
            int distanceToEnemy = calculateDistance(husky.getX(), husky.getY(), 
                                                   nearestEnemy.getX(), nearestEnemy.getY());
            // If enemy is close and we're near hero, move towards enemy
            if (distanceToEnemy <= 4 && distanceToEnemy > MELEE_ATTACK_RANGE) {
                targetX = nearestEnemy.getX();
                targetY = nearestEnemy.getY();
            } else {
                targetX = hero.getX();
                targetY = hero.getY();
            }
        } else {
            // Stay close to hero
            targetX = hero.getX();
            targetY = hero.getY();
        }

        Point nextPosition = calculateNextPosition(husky, targetX, targetY, tileMap);
        
        if (nextPosition.getX() != husky.getX() || nextPosition.getY() != husky.getY()) {
            husky.setX(nextPosition.getX());
            husky.setY(nextPosition.getY());
        }
    }

    private Point calculateNextPosition(Husky husky, int targetX, int targetY, TileMap tileMap) {
        int currentX = husky.getX();
        int currentY = husky.getY();

        // Don't move if already at target or adjacent
        int distance = calculateDistance(currentX, currentY, targetX, targetY);
        if (distance <= 1) {
            return new Point(currentX, currentY);
        }

        Point nextPosition = null;

        // Try moving horizontally first
        if (currentX < targetX) {
            nextPosition = tryMove(tileMap, currentX, currentY, currentX + 1, currentY);
        } else if (currentX > targetX) {
            nextPosition = tryMove(tileMap, currentX, currentY, currentX - 1, currentY);
        }

        // If horizontal move didn't work, try vertical
        if (nextPosition == null) {
            if (currentY < targetY) {
                nextPosition = tryMove(tileMap, currentX, currentY, currentX, currentY + 1);
            } else if (currentY > targetY) {
                nextPosition = tryMove(tileMap, currentX, currentY, currentX, currentY - 1);
            }
        }

        return nextPosition != null ? nextPosition : new Point(currentX, currentY);
    }

    private Point tryMove(TileMap tileMap, int currentX, int currentY, int newX, int newY) {
        if (!collisionDetectionService.isCollisionWithObstacleOrOutOfBounds(tileMap, newX, newY)) {
            return new Point(newX, newY);
        }
        return null;
    }

    private void performMeleeAttack(Husky husky, IEnemy enemy) {
        int damage = husky.getAttackingPower();
        enemy.decreaseHealth(damage);
        logger.info("Husky melee attacked enemy at (" + enemy.getX() + ", " + enemy.getY() + ") for " + damage + " damage");
    }

    private void performBarkAttack(Husky husky, IEnemy enemy) {
        husky.bark();
        
        // Calculate direction to enemy
        MoveDirection direction = calculateDirection(husky.getX(), husky.getY(), 
                                                     enemy.getX(), enemy.getY());
        
        // Launch wind projectile
        WindProjectile projectile = new WindProjectile(
            husky.getX(), 
            husky.getY(), 
            direction, 
            PROJECTILE_RANGE, 
            PROJECTILE_DAMAGE
        );
        
        projectileService.launchProjectile(projectile);
        logger.info("Husky barked and launched wind projectile towards enemy");
    }

    private MoveDirection calculateDirection(int fromX, int fromY, int toX, int toY) {
        int dx = toX - fromX;
        int dy = toY - fromY;
        
        // Prioritize the axis with larger difference
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? MoveDirection.RIGHT : MoveDirection.LEFT;
        } else {
            return dy > 0 ? MoveDirection.DOWN : MoveDirection.UP;
        }
    }

    private IEnemy findNearestEnemy(Husky husky, Collection<IEnemy> enemies) {
        IEnemy nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (IEnemy enemy : enemies) {
            int distance = calculateDistance(husky.getX(), husky.getY(), 
                                            enemy.getX(), enemy.getY());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }

        return nearest;
    }

    private int calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    @Override
    public Collection<ICompanion> getCompanions() {
        return List.copyOf(companions);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
}
