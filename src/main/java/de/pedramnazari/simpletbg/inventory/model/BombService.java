package de.pedramnazari.simpletbg.inventory.model;

import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.inventory.service.ItemService;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.ui.controller.GameWorldController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BombService implements Runnable {

    private static final Logger logger = Logger.getLogger(BombService.class.getName());

    private final List<Bomb> bombs = new ArrayList<>();
    private final ItemService itemService;
    private final HeroService heroService;
    private final EnemyService enemyService;
    private final GameWorldController gameWorldController;

    private boolean running = false;

    // TODO: remove GameWorldController and use events instead.
    public BombService(ItemService itemService, HeroService heroService , EnemyService enemyService, GameWorldController gameWorldController) {
        this.itemService = itemService;
        this.heroService = heroService;
        this.enemyService = enemyService;
        this.gameWorldController = gameWorldController;
    }


    public void placeBomb(Bomb bomb) {
        synchronized (bombs) {
            bombs.add(bomb);
        }
        itemService.addItem(bomb);
        gameWorldController.updateItems();

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
    public void run() {
        while (running) {
            try {
                Thread.sleep(100);
                final List<Bomb> explodedBombs = new ArrayList<>();

                synchronized (bombs) {
                    for (Bomb bomb : bombs) {
                        if (bomb.shouldTriggerEffect()) {
                            explodeBomb(bomb);
                        }
                        else if (bomb.isExplosionOngoing()) {
                            logger.info("Bomb explosion ongoing.");
                            executeBombAttack(bomb);
                        }
                        else if (bomb.isExplosionFinished()) {
                            itemService.removeItem(bomb);
                            gameWorldController.bombExplosionFinished(bomb);
                            explodedBombs.add(bomb);
                        }

                    }
                    bombs.removeAll(explodedBombs);
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
        return heroService.getHeroAttackService().heroAttacksUsingWeapon(bomb, heroService.getHero(), enemyService.getEnemies());
    }
}
