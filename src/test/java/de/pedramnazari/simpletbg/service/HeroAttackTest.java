package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.DefaultHeroFactory;
import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.character.hero.service.HeroMovementService;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.model.TileMapTestHelper;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
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
        final Hero hero = heroService.getHero(); // place hero in the middle of the map


        assertFalse(hero.getMoveDirection().isPresent());

        final Collection<Enemy> enemies = List.of();

        List<Point> attackPoints = heroService.heroAttacks(enemies);
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
}
