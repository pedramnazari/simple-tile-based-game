package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.tilemap.model.IArmor;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IEnemyService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class ArmorService {

    private static final Logger logger = Logger.getLogger(ArmorService.class.getName());

    private final IEnemyService enemyService;
    private final List<ArmorAttackListener> listeners = new ArrayList<>();

    public ArmorService(IEnemyService enemyService) {
        this.enemyService = enemyService;
    }

    public void addArmorAttackListener(ArmorAttackListener listener) {
        listeners.add(listener);
    }

    /**
     * Perform auto-attacks for the hero's armor if equipped.
     * This should be called periodically from the game loop.
     * 
     * @param hero The hero whose armor will attack
     * @param gameContext The game context (currently unused, reserved for future use)
     */
    public void performAutoAttacks(IHero hero, GameContext gameContext) {
        if (hero.getArmor().isEmpty()) {
            return;
        }

        IArmor armor = hero.getArmor().get();
        if (!armor.canAttack()) {
            return;
        }

        // Find enemies within attack range
        IEnemy targetEnemy = findNearestEnemyInRange(hero, armor.getAttackRange());
        if (targetEnemy == null) {
            return;
        }

        // Perform attack
        int damage = armor.getAttackingDamage();
        targetEnemy.decreaseHealth(damage);
        armor.recordAttack();

        logger.info(String.format("Armor auto-attack: dealt %d damage to enemy at (%d, %d). Enemy health: %d",
                damage, targetEnemy.getX(), targetEnemy.getY(), targetEnemy.getHealth()));

        // Notify listeners about the attack for visual effects
        notifyArmorAttack(hero, targetEnemy, armor);

        // Check if enemy is dead
        if (targetEnemy.getHealth() <= 0) {
            // Enemy death is handled by the visualizer when it updates the enemy
            logger.info("Armor killed enemy at (" + targetEnemy.getX() + ", " + targetEnemy.getY() + ")");
        }
    }

    private IEnemy findNearestEnemyInRange(IHero hero, int range) {
        Collection<IEnemy> enemies = enemyService.getEnemies();
        IEnemy nearestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        int heroX = hero.getX();
        int heroY = hero.getY();

        for (IEnemy enemy : enemies) {
            if (enemy.getHealth() <= 0) {
                continue;
            }

            int enemyX = enemy.getX();
            int enemyY = enemy.getY();

            // Calculate Euclidean distance
            double distance = Math.sqrt(Math.pow(heroX - enemyX, 2) + Math.pow(heroY - enemyY, 2));

            // Check if within range
            if (distance <= range && distance < minDistance) {
                nearestEnemy = enemy;
                minDistance = distance;
            }
        }

        return nearestEnemy;
    }

    private void notifyArmorAttack(IHero hero, IEnemy target, IArmor armor) {
        for (ArmorAttackListener listener : listeners) {
            listener.onArmorAttack(hero, target, armor);
        }
    }

    /**
     * Listener interface for armor attack events
     */
    public interface ArmorAttackListener {
        void onArmorAttack(IHero hero, IEnemy target, IArmor armor);
    }
}
