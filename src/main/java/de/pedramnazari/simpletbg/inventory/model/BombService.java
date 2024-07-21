package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.IHeroService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class BombService implements Runnable {

    private static final Logger logger = Logger.getLogger(BombService.class.getName());

    private final List<Bomb> bombs = new ArrayList<>();
    private final IHeroService heroService;
    private final IEnemyService enemyService;
    private final GameWorldController gameWorldController;

    private boolean running = false;

    // TODO: remove GameWorldController and use events instead.
    public BombService(IHeroService heroService, IEnemyService enemyService, GameWorldController gameWorldController) {
        this.heroService = heroService;
        this.enemyService = enemyService;
        this.gameWorldController = gameWorldController;
    }


    public void placeBomb(Bomb bomb) {
        synchronized (bombs) {
            bombs.add(bomb);
            logger.info("Bomb placed: " + bomb);
        }
        gameWorldController.updateBombs(bombs);

        if (!running) {
            startService();
        }
    }

    private void startService() {
        Thread bombThread = new Thread(this);
        running = true;
        bombThread.start();
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void removeBomb(Bomb bomb) {
        synchronized (bombs) {
            bombs.remove(bomb);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(100);
                final List<Bomb> explodedBombs = new ArrayList<>();

                synchronized (getBombs()) {
                    for (Bomb bomb : getBombs()) {
                        if (bomb.shouldTriggerEffect()) {
                            explodeBomb(bomb);
                        }
                        else if (bomb.isExplosionOngoing()) {
                            executeBombAttack(bomb);
                        }
                        else if (bomb.isExplosionFinished()) {
                            explodedBombs.add(bomb);
                        }
                    }
                }

                // Remove exploded bombs outside synchronized block
                // to avoid concurrent modification issues
                for (Bomb bomb : explodedBombs) {
                    removeBomb(bomb);
                    gameWorldController.bombExplosionFinished(bomb);
                }
            } catch (InterruptedException e) {
                logger.severe("BombService interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }


    public void stopService() {
        running = false;
    }

    private void explodeBomb(Bomb bomb) {
        logger.info("Bomb exploded: " + bomb);
        bomb.triggerEffect();

        List<Point> attackPoints = executeBombAttack(bomb);

        gameWorldController.bombExploded(bomb, attackPoints);
    }

    private List<Point> executeBombAttack(Bomb bomb) {
        return this.heroAttacksUsingWeapon(bomb, heroService.getHero(), enemyService.getEnemies());
    }

    public List<Point> heroAttacksUsingWeapon(final Bomb bomb, final IHero hero, final Collection<IEnemy> enemies) {
        int xPos = bomb.getX();
        int yPos = bomb.getY();

        final List<Point> attackPoints = determineAttackPoints(bomb, xPos, yPos);

        if (attackPoints.isEmpty()) {
            return List.of();
        }

        int damage = bomb.getAttackingDamage();

        notifyBombAttacksCharacter(bomb, hero, enemies, attackPoints, damage);

        return attackPoints;
    }

    private void notifyBombAttacksCharacter(Bomb bomb, IHero hero, Collection<IEnemy> enemies, List<Point> attackPoints, int damage) {
        for (Point attackPoint : attackPoints) {
            if ((hero.getX() == attackPoint.getX()) && (hero.getY() == attackPoint.getY())) {
                logger.info("Bomb attacks hero at position: " + attackPoint);
                gameWorldController.onHeroHitByBomb(hero, bomb, damage);
            }

            for (IEnemy enemy : enemies) {
                if ((enemy.getX() == attackPoint.getX()) && (enemy.getY() == attackPoint.getY())) {
                    logger.info("Bomb attacks enemy at position: " + attackPoint);
                    gameWorldController.onEnemyHitByBomb(enemy, bomb, damage);
                }
            }

        }
    }

    private List<Point> determineAttackPoints(Bomb bomb, int xPos, int yPos) {
        final List<Point> attackPoints = new ArrayList<>();
        // Attack also characters in same position as bomb
        attackPoints.add(new Point(xPos, yPos));

        final int range = bomb.getRange();

        attackPoints.addAll(determineAttackPointsForDirection(range, xPos, yPos, MoveDirection.LEFT));
        attackPoints.addAll(determineAttackPointsForDirection(range, xPos, yPos, MoveDirection.RIGHT));
        attackPoints.addAll(determineAttackPointsForDirection(range, xPos, yPos, MoveDirection.UP));
        attackPoints.addAll(determineAttackPointsForDirection(range, xPos, yPos, MoveDirection.DOWN));

        return attackPoints;
    }

    private List<Point> determineAttackPointsForDirection(int weaponRange, int xPos, int yPos, final MoveDirection moveDirection) {
        final List<Point> attackPoints = new ArrayList<>();
        int targetY;
        int targetX;
        if (moveDirection != null) {
            for (int i = 1; i <= weaponRange; i++) {
                switch (moveDirection) {
                    case UP -> {
                        targetX = xPos;
                        targetY = yPos - i;
                    }
                    case DOWN -> {
                        targetX = xPos;
                        targetY = yPos + i;
                    }
                    case LEFT -> {
                        targetX = xPos - i;
                        targetY = yPos;
                    }
                    case RIGHT -> {
                        targetX = xPos + i;
                        targetY = yPos;
                    }
                    default -> {
                        targetX = xPos;
                        targetY = yPos;
                    }
                }

                // TODO: remove dependency to GameContext
                final CollisionDetectionService collisionDetectionService = GameContext.getInstance().getHeroService().getCollisionDetectionService();
                final TileMap tileMap = GameContext.getInstance().getTileMap();

                if (collisionDetectionService.isCollisionWithObstacleOrOutOfBounds(tileMap, targetX, targetY)) {
                    break;
                }

                attackPoints.add(new Point(targetX, targetY));
            }
        }

        return attackPoints;
    }

}
