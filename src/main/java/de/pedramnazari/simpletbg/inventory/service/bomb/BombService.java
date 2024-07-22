package de.pedramnazari.simpletbg.inventory.service.bomb;

import de.pedramnazari.simpletbg.character.hero.service.HeroAttackNotifier;
import de.pedramnazari.simpletbg.character.hero.service.IHeroAttackNotifier;
import de.pedramnazari.simpletbg.character.service.HeroHitNotifier;
import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.character.service.IHeroHitListener;
import de.pedramnazari.simpletbg.inventory.model.bomb.IBombEventListener;
import de.pedramnazari.simpletbg.inventory.model.bomb.IBombService;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.IHeroService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class BombService implements Runnable, IBombService {

    private static final Logger logger = Logger.getLogger(BombService.class.getName());

    private final List<IBombEventListener> bombEventListeners = new ArrayList<>();
    private final HeroHitNotifier heroHitNotifier = new HeroHitNotifier();
    // TODO: replace with something like bombAttackNotifier?
    private final IHeroAttackNotifier heroAttackNotifier = new HeroAttackNotifier();

    private final List<IBomb> bombs = new ArrayList<>();
    private final IHeroService heroService;
    private final IEnemyService enemyService;

    private boolean running = false;

    // TODO: remove GameWorldController and use events instead.
    public BombService(IHeroService heroService, IEnemyService enemyService) {
        this.heroService = heroService;
        this.enemyService = enemyService;
    }


    @Override
    public void placeBomb(IBomb bomb) {
        synchronized (bombs) {
            bombs.add(bomb);
            logger.info("Bomb placed: " + bomb);
        }
        notifyBombPlaced(bomb);


        if (!running) {
            startService();
        }
    }

    private void startService() {
        Thread bombThread = new Thread(this);
        running = true;
        bombThread.start();
    }

    @Override
    public List<IBomb> getBombs() {
        return bombs;
    }

    @Override
    public void removeBomb(IBomb bomb) {
        synchronized (bombs) {
            bombs.remove(bomb);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(100);
                final List<IBomb> explodedBombs = new ArrayList<>();

                synchronized (getBombs()) {
                    for (IBomb bomb : getBombs()) {
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
                for (IBomb bomb : explodedBombs) {
                    removeBomb(bomb);
                    notifyOnBombExplosionFinished(bomb);
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

    private void explodeBomb(IBomb bomb) {
        logger.info("Bomb exploded: " + bomb);
        bomb.triggerEffect();

        List<Point> attackPoints = executeBombAttack(bomb);

        notifyOnBombExploded(bomb, attackPoints);
    }

    private List<Point> executeBombAttack(IBomb bomb) {
        return this.heroAttacksUsingWeapon(bomb, heroService.getHero(), enemyService.getEnemies());
    }

    public List<Point> heroAttacksUsingWeapon(final IBomb bomb, final IHero hero, final Collection<IEnemy> enemies) {
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

    private void notifyBombAttacksCharacter(IBomb bomb, IHero hero, Collection<IEnemy> enemies, List<Point> attackPoints, int damage) {
        for (Point attackPoint : attackPoints) {
            if ((hero.getX() == attackPoint.getX()) && (hero.getY() == attackPoint.getY())) {
                logger.info("Bomb attacks hero at position: " + attackPoint);

                heroHitNotifier.notifyHeroHit(hero, bomb, damage);
            }

            for (IEnemy enemy : enemies) {
                if ((enemy.getX() == attackPoint.getX()) && (enemy.getY() == attackPoint.getY())) {
                    logger.info("Bomb attacks enemy at position: " + attackPoint);

                    heroAttackNotifier.notifyHeroAttacksCharacter(enemy, damage);
                }
            }

        }
    }

    private List<Point> determineAttackPoints(IBomb bomb, int xPos, int yPos) {
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

    public void addBombEventListener(IBombEventListener listener) {
        bombEventListeners.add(listener);
    }

    public void removeBombEventListener(IBombEventListener listener) {
        bombEventListeners.remove(listener);
    }

    private void notifyBombPlaced(IBomb newBomb) {
        for (IBombEventListener listener : bombEventListeners) {
            listener.onBombPlaced(newBomb, bombs);
        }
    }

    private void notifyOnBombExploded(IBomb bomb, List<Point> attackPoints) {
        for (IBombEventListener listener : bombEventListeners) {
            listener.onBombExploded(bomb, attackPoints);
        }
    }

    private void notifyOnBombExplosionFinished(IBomb bomb) {
        for (IBombEventListener listener : bombEventListeners) {
            listener.onBombExplosionFinished(bomb);
        }
    }

    public void addHeroHitListener(IHeroHitListener listener) {
        heroHitNotifier.addListener(listener);
    }

    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackNotifier.addListener(listener);
    }

}
