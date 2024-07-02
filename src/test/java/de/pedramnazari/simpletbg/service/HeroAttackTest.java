package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.service.DefaultEnemyFactory;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyMovementService;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.service.HeroService;
import de.pedramnazari.simpletbg.game.service.GameWorldService;
import de.pedramnazari.simpletbg.model.TileMapTestHelper;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import org.junit.jupiter.api.Test;

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

        final Hero hero = new Hero(2, 2); // place hero in the middle of the map
        final HeroServiceMock heroService = new HeroServiceMock(hero, tileMap);

        final EnemyService enemyService = new EnemyService(new DefaultEnemyFactory(new CollisionDetectionService()), new EnemyMovementService(new CollisionDetectionService()));
        final GameWorldService gameWorldService = new GameWorldService(null, null, heroService, enemyService);

        assertFalse(hero.getMoveDirection().isPresent());

        List<Point> attackPoints = gameWorldService.heroAttacks();
        assertEquals(1, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));

        hero.setMoveDirection(MoveDirection.RIGHT);
        attackPoints = gameWorldService.heroAttacks();
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX() + 1, hero.getY()));

        hero.setMoveDirection(MoveDirection.LEFT);
        attackPoints = gameWorldService.heroAttacks();
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX() - 1, hero.getY()));

        hero.setMoveDirection(MoveDirection.UP);
        attackPoints = gameWorldService.heroAttacks();
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX(), hero.getY() - 1));

        hero.setMoveDirection(MoveDirection.DOWN);
        attackPoints = gameWorldService.heroAttacks();
        assertEquals(2, attackPoints.size());
        assertEquals(attackPoints.get(0), new Point(hero.getX(), hero.getY()));
        assertEquals(attackPoints.get(1), new Point(hero.getX(), hero.getY() + 1));
    }


    class HeroServiceMock extends HeroService {
        private final Hero hero;
        private final TileMap tileMap;

        public HeroServiceMock(final Hero hero, final TileMap tileMap) {
            super(null, null);
            this.hero = hero;
            this.tileMap = tileMap;
        }

        @Override
        public Hero getHero() {
            return hero;
        }

        public TileMap getTileMap() {
            return tileMap;
        }
    }
}
