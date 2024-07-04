package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.Weapon;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class HeroAttackService implements IHeroAttackNotifier {

    private static final Logger logger = Logger.getLogger(HeroAttackService.class.getName());

    private final IHeroAttackNotifier heroAttackNotifier = new HeroAttackNotifier();

    public List<Point> heroAttacks(final Hero hero, final Collection<Enemy> enemies) {
        // Hero can only make damage if he has a weapon
        if (hero.getWeapon().isEmpty()) {
            logger.info("Hero tries to attack without weapon.");
            return List.of();
        }

        final Weapon weapon = hero.getWeapon().get();

        final int damage = weapon.getAttackingDamage();

        final List<Point> attackPoints = new ArrayList<>();
        // Attack also enemies in same position as hero
        attackPoints.add(new Point(hero.getX(), hero.getY()));

        int targetX, targetY;

        MoveDirection moveDirection = hero.getMoveDirection().orElse(null);
        if (moveDirection != null) {
            switch (moveDirection) {
                case UP -> {
                    targetX = hero.getX();
                    targetY = hero.getY() - 1;
                }
                case DOWN -> {
                    targetX = hero.getX();
                    targetY = hero.getY() + 1;
                }
                case LEFT -> {
                    targetX = hero.getX() - 1;
                    targetY = hero.getY();
                }
                case RIGHT -> {
                    targetX = hero.getX() + 1;
                    targetY = hero.getY();
                }
                default -> {
                    targetX = hero.getX();
                    targetY = hero.getY();
                }
            }

            attackPoints.add(new Point(targetX, targetY));
        }

        for (Point attackPoint : attackPoints) {
            for (Enemy enemy : enemies) {
                if ((enemy.getX() == attackPoint.getX()) && (enemy.getY() == attackPoint.getY())) {
                    notifyHeroAttacksCharacter(enemy, damage);
                }
            }
        }

        return attackPoints;

    }

    @Override
    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackNotifier.addHeroAttackListener(listener);
    }

    @Override
    public void notifyHeroAttacksCharacter(Character attackedCharacter, int damage) {
        heroAttackNotifier.notifyHeroAttacksCharacter(attackedCharacter, damage);
    }
}
