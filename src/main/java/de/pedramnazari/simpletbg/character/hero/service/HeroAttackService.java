package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.tilemap.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class HeroAttackService implements IHeroAttackNotifier {

    private static final Logger logger = Logger.getLogger(HeroAttackService.class.getName());

    private final IHeroAttackNotifier heroAttackNotifier = new HeroAttackNotifier();

    public List<Point> heroAttacks(final IHero hero, final Collection<IEnemy> enemies) {
        // Hero can only make damage if he has a weapon
        if (hero.getWeapon().isEmpty()) {
            logger.info("Hero tries to attack without weapon.");
            return List.of();
        }

        final IWeapon weapon = hero.getWeapon().get();

        final List<Point> attackPoints = new ArrayList<>();
        // Attack also enemies in same position as hero
        attackPoints.add(new Point(hero.getX(), hero.getY()));

        final MoveDirection moveDirection = hero.getMoveDirection().orElse(null);

        if (weapon.canAttackInAllDirections()) {
            attackPoints.addAll(determineAttackPoints(hero, MoveDirection.LEFT));
            attackPoints.addAll(determineAttackPoints(hero, MoveDirection.RIGHT));
            attackPoints.addAll(determineAttackPoints(hero, MoveDirection.UP));
            attackPoints.addAll(determineAttackPoints(hero, MoveDirection.DOWN));
        }
        else if (weapon.canAttackBackward() && (moveDirection != null)) {
            attackPoints.addAll(determineAttackPoints(hero, moveDirection));
            attackPoints.addAll(determineAttackPoints(hero, MoveDirection.getOppositeDirection(moveDirection)));
        }
        else {
            attackPoints.addAll(determineAttackPoints(hero, moveDirection));
        }


        int damage = hero.getAttackingPower() + weapon.getAttackingDamage();

        if (hero.getRing().isPresent()) {
            IRing ring = hero.getRing().get();
            damage += ring.getAttackingPower();
        }

        for (Point attackPoint : attackPoints) {
            for (IEnemy enemy : enemies) {
                if ((enemy.getX() == attackPoint.getX()) && (enemy.getY() == attackPoint.getY())) {
                    notifyHeroAttacksCharacter(enemy, damage);
                }
            }
        }

        return attackPoints;
    }

    private List<Point> determineAttackPoints(IHero hero, final MoveDirection moveDirection) {
        final List<Point> attackPoints = new ArrayList<>();
        int targetY;
        int targetX;
        if (moveDirection != null) {
            for (int i = 1; i <= hero.getWeapon().get().getRange(); i++) {
                switch (moveDirection) {
                    case UP -> {
                        targetX = hero.getX();
                        targetY = hero.getY() - i;
                    }
                    case DOWN -> {
                        targetX = hero.getX();
                        targetY = hero.getY() + i;
                    }
                    case LEFT -> {
                        targetX = hero.getX() - i;
                        targetY = hero.getY();
                    }
                    case RIGHT -> {
                        targetX = hero.getX() + i;
                        targetY = hero.getY();
                    }
                    default -> {
                        targetX = hero.getX();
                        targetY = hero.getY();
                    }
                }

                System.out.println("targetX: " + targetX + " targetY: " + targetY);

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
