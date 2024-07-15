package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.Bomb;
import de.pedramnazari.simpletbg.inventory.model.BombPlacer;
import de.pedramnazari.simpletbg.tilemap.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class HeroAttackService implements IHeroAttackNotifier {

    private static final Logger logger = Logger.getLogger(HeroAttackService.class.getName());

    private final IHeroAttackNotifier heroAttackNotifier = new HeroAttackNotifier();

    public List<Point> heroAttacks(final IHero hero, final Collection<IEnemy> enemies) {
        return heroAttacksUsingWeapon(hero.getWeapon().orElse(null), hero, enemies);
    }

    public List<Point> heroAttacksUsingWeapon(final IWeapon weapon, final IHero hero, final Collection<IEnemy> enemies) {
        // Hero can only make damage if he has a weapon
        if (weapon == null) {
            logger.info("Hero tries to attack without weapon.");
            return List.of();
        }

        if (weapon instanceof BombPlacer bombPlacer) {
            bombPlacer.placeBomb(hero.getX(), hero.getY());
            return List.of();
        }

        int xPos = hero.getX();
        int yPos = hero.getY();

        if (weapon instanceof Bomb) {
            xPos = weapon.getX();
            yPos = weapon.getY();
        }

        logger.info("Hero attacks using weapon: " + weapon.getName() + " with range " + weapon.getRange());

        final List<Point> attackPoints = new ArrayList<>();
        // Attack also enemies in same position as hero
        attackPoints.add(new Point(xPos, yPos));

        final MoveDirection moveDirection = hero.getMoveDirection().orElse(null);
        final int range = weapon.getRange();

        if (weapon.canAttackInAllDirections()) {
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, MoveDirection.LEFT));
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, MoveDirection.RIGHT));
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, MoveDirection.UP));
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, MoveDirection.DOWN));
        }
        else if (weapon.canAttackBackward() && (moveDirection != null)) {
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, moveDirection));
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, MoveDirection.getOppositeDirection(moveDirection)));
        }
        else {
            attackPoints.addAll(determineAttackPoints(range, xPos, yPos, moveDirection));
        }

        int damage = hero.getAttackingPower() + weapon.getAttackingDamage();

        if (hero.getRing().isPresent()) {
            IRing ring = hero.getRing().get();
            damage += ring.getAttackingPower();
        }

        for (Point attackPoint : attackPoints) {
            for (IEnemy enemy : enemies) {
                if ((enemy.getX() == attackPoint.getX()) && (enemy.getY() == attackPoint.getY())) {
                    logger.info("Hero attacks enemy at position: " + attackPoint);
                    notifyHeroAttacksCharacter(enemy, damage);
                }
            }
        }

        return attackPoints;
    }

    private List<Point> determineAttackPoints(int weaponRange, int xPos, int yPos, final MoveDirection moveDirection) {
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

                logger.info("Hero attacks at position: " + targetX + ", " + targetY);

                attackPoints.add(new Point(targetX, targetY));
            }
        }

        return attackPoints;
    }

    @Override
    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackNotifier.addHeroAttackListener(listener);
    }

    @Override
    public void notifyHeroAttacksCharacter(ICharacter attackedCharacter, int damage) {
        heroAttackNotifier.notifyHeroAttacksCharacter(attackedCharacter, damage);
    }
}
