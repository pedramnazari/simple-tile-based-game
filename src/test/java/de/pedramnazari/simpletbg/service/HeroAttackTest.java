package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.inventory.service.DefaultItemFactory;
import de.pedramnazari.simpletbg.model.TileMapTestHelper;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HeroAttackTest {

    private static final int F = TileType.FLOOR.getType();

    @Test
    public void testHeroAttacks() {
        final TileMap tileMap = TileMapTestHelper.createMapUsingDefaults(new int[][]{
                {F, F, F, F, F,},
                {F, F, F, F, F,},
                {F, F, F, F, F,},
                {F, F, F, F, F,},
                {F, F, F, F, F,},
        });

        final HeroService heroService = new HeroService(
                new DefaultHeroFactory(),
                new HeroMovementService(new CollisionDetectionService()),
                new HeroAttackService()
        );

        heroService.init(2, 2);
        final IHero hero = heroService.getHero(); // place hero in the middle of the map


        assertFalse(hero.getMoveDirection().isPresent());

        final Collection<IEnemy> enemies = List.of();

        List<Point> attackPoints = heroService.heroAttacks(enemies);
        // Hero does not have a weapon, so he can't attack
        assertEquals(0, attackPoints.size());

        // Give hero a weapon
        hero.setWeapon(createWeapon(TileType.WEAPON_SWORD));

        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(1, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));

        hero.setMoveDirection(MoveDirection.RIGHT);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX() + 1, hero.getY()));

        hero.setMoveDirection(MoveDirection.LEFT);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX() - 1, hero.getY()));

        hero.setMoveDirection(MoveDirection.UP);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX(), hero.getY() - 1));

        hero.setMoveDirection(MoveDirection.DOWN);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX(), hero.getY() + 1));
    }

    @Test
    public void testHeroAttacks_withLongRange() {
        final TileMap tileMap = TileMapTestHelper.createMapUsingDefaults(new int[][]{
                {F, F, F, F, F,},
                {F, F, F, F, F,},
                {F, F, F, F, F,},
                {F, F, F, F, F,},
                {F, F, F, F, F,},
        });

        final HeroService heroService = new HeroService(
                new DefaultHeroFactory(),
                new HeroMovementService(new CollisionDetectionService()),
                new HeroAttackService()
        );

        heroService.init(2, 2);
        final IHero hero = heroService.getHero(); // place hero in the middle of the map

        assertFalse(hero.getMoveDirection().isPresent());

        final Collection<IEnemy> enemies = List.of();

        List<Point> attackPoints = heroService.heroAttacks(enemies);

        // Give hero a long range weapon
        hero.setWeapon(createWeapon(TileType.WEAPON_LANCE));

        assertEquals(2, hero.getWeapon().get().getRange());

        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(1, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));

        hero.setMoveDirection(MoveDirection.RIGHT);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(   3, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX() + 1, hero.getY()));
        assertEquals(attackPoints.get(2), new Point(hero.getX() + 2, hero.getY()));

        hero.setMoveDirection(MoveDirection.DOWN);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(3, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX(), hero.getY() + 1));
        assertEquals(attackPoints.get(2), new Point(hero.getX(), hero.getY() + 2));

        hero.setMoveDirection(MoveDirection.LEFT);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(3, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX() - 1, hero.getY()));
        assertEquals(attackPoints.get(2), new Point(hero.getX() - 2, hero.getY()));

        hero.setMoveDirection(MoveDirection.UP);
        attackPoints = heroService.heroAttacks(enemies);
        assertEquals(3, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX(), hero.getY() - 1));
        assertEquals(attackPoints.get(2), new Point(hero.getX(), hero.getY() - 2));
    }

    private IWeapon createWeapon(TileType weaponType) {
        return (IWeapon) new DefaultItemFactory().createElement(weaponType.getType(), 0, 0);
    }
}
