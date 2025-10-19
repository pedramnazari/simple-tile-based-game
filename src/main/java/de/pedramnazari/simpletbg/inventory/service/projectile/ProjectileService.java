package de.pedramnazari.simpletbg.inventory.service.projectile;


import de.pedramnazari.simpletbg.inventory.service.IWeaponDealsDamageListener;
import de.pedramnazari.simpletbg.inventory.service.WeaponDealsDamageNotifier;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.ITileMapService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ProjectileService implements Runnable, IProjectileService {

    private static final Logger logger = Logger.getLogger(ProjectileService.class.getName());

    private final List<IProjectileEventListener> projectileEventListeners = new ArrayList<>();
    private final WeaponDealsDamageNotifier weaponDealsDamageNotifier = new WeaponDealsDamageNotifier();
    private final List<IProjectile> projectiles = new CopyOnWriteArrayList<>();

    private final CollisionDetectionService collisionDetectionService;
    private final ITileMapService tileMapService;
    private final IEnemyService enemyService;

    private volatile boolean running = false;

    public ProjectileService(CollisionDetectionService collisionDetectionService,
                             ITileMapService tileMapService,
                             IEnemyService enemyService) {
        this.collisionDetectionService = collisionDetectionService;
        this.tileMapService = tileMapService;
        this.enemyService = enemyService;
    }

    @Override
    public void launchProjectile(IProjectile projectile) {
        if (projectile == null) {
            return;
        }

        projectiles.add(projectile);
        notifyProjectileCreated(projectile);

        if (!running) {
            startService();
        }
    }

    private void startService() {
        Thread projectileThread = new Thread(this);
        projectileThread.setDaemon(true);
        running = true;
        projectileThread.start();
    }

    @Override
    public void addProjectileEventListener(IProjectileEventListener listener) {
        projectileEventListeners.add(listener);
    }

    @Override
    public void addWeaponDealsDamageListener(IWeaponDealsDamageListener listener) {
        weaponDealsDamageNotifier.addListener(listener);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (projectiles.isEmpty()) {
                continue;
            }

            List<IProjectile> finishedProjectiles = new ArrayList<>();

            for (IProjectile projectile : projectiles) {
                if (!projectile.isActive()) {
                    finishedProjectiles.add(projectile);
                    continue;
                }

                if (projectile.getRemainingRange() <= 0) {
                    projectile.deactivate();
                    finishedProjectiles.add(projectile);
                    continue;
                }

                Optional<MoveDirection> moveDirection = projectile.getMoveDirection();

                if (moveDirection.isEmpty()) {
                    projectile.deactivate();
                    finishedProjectiles.add(projectile);
                    continue;
                }

                Point nextPoint = calcNextPoint(projectile.getX(), projectile.getY(), moveDirection.get());
                TileMap tileMap = tileMapService.getTileMap();

                if (collisionDetectionService.isCollisionWithObstacleOrOutOfBounds(tileMap, nextPoint.getX(), nextPoint.getY())) {
                    projectile.deactivate();
                    finishedProjectiles.add(projectile);
                    continue;
                }

                projectile.setX(nextPoint.getX());
                projectile.setY(nextPoint.getY());
                projectile.setRemainingRange(projectile.getRemainingRange() - 1);

                notifyProjectileMoved(projectile);

                if (handleEnemyCollision(projectile)) {
                    projectile.deactivate();
                    finishedProjectiles.add(projectile);
                    continue;
                }

                if (projectile.getRemainingRange() == 0) {
                    projectile.deactivate();
                    finishedProjectiles.add(projectile);
                }
            }

            for (IProjectile projectile : finishedProjectiles) {
                projectiles.remove(projectile);
                notifyProjectileFinished(projectile);
            }
        }
    }

    private boolean handleEnemyCollision(IProjectile projectile) {
        Collection<IEnemy> enemies = enemyService.getEnemies();
        boolean hit = false;
        for (IEnemy enemy : enemies) {
            if ((enemy.getX() == projectile.getX()) && (enemy.getY() == projectile.getY())) {
                logger.info("Projectile hit enemy at position: " + projectile.getX() + ", " + projectile.getY());
                weaponDealsDamageNotifier.notifyWeaponDealsDamage(projectile.getWeapon(), enemy, projectile.getDamage());
                hit = true;
            }
        }
        return hit;
    }

    private Point calcNextPoint(int x, int y, MoveDirection direction) {
        return switch (direction) {
            case UP -> new Point(x, y - 1);
            case DOWN -> new Point(x, y + 1);
            case LEFT -> new Point(x - 1, y);
            case RIGHT -> new Point(x + 1, y);
        };
    }

    private void notifyProjectileCreated(IProjectile projectile) {
        projectileEventListeners.forEach(listener -> listener.onProjectileCreated(projectile));
    }

    private void notifyProjectileMoved(IProjectile projectile) {
        projectileEventListeners.forEach(listener -> listener.onProjectileMoved(projectile));
    }

    private void notifyProjectileFinished(IProjectile projectile) {
        projectileEventListeners.forEach(listener -> listener.onProjectileFinished(projectile));
    }
}
