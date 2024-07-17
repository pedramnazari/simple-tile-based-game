package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;
import de.pedramnazari.simpletbg.tilemap.service.IHeroService;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;

import java.util.ArrayList;
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
    public BombService(IHeroService heroService , IEnemyService enemyService, GameWorldController gameWorldController) {
        this.heroService = heroService;
        this.enemyService = enemyService;
        this.gameWorldController = gameWorldController;
    }


    public void placeBomb(Bomb bomb) {
        synchronized (bombs) {
            bombs.add(bomb);
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
                        } else if (bomb.isExplosionOngoing()) {
                            executeBombAttack(bomb);
                        } else if (bomb.isExplosionFinished()) {
                            explodedBombs.add(bomb);
                        }
                    }
                }

                // Remove exploded bombs outside of synchronized block
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
        logger.info("Bomb exploded.");
        bomb.triggerEffect();

        List<Point> attackPoints = executeBombAttack(bomb);

        gameWorldController.bombExploded(bomb, attackPoints);
    }

    private List<Point> executeBombAttack(Bomb bomb) {
        return heroService.heroAttacksUsingWeapon(bomb, heroService.getHero(), enemyService.getEnemies());
    }
}
