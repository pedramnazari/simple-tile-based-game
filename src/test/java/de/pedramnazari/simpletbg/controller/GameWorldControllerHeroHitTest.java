package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.drivers.ui.controller.GameWorldController;
import de.pedramnazari.simpletbg.drivers.ui.view.GameWorldVisualizer;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameWorldControllerHeroHitTest {

    private GameWorldController controller;
    private TestGameWorldVisualizer visualizer;

    @BeforeEach
    void setUp() {
        controller = new GameWorldController(null);
        visualizer = new TestGameWorldVisualizer();
        controller.setTileMapVisualizer(visualizer);
    }

    @Test
    void heroHitByEnemyReducesHealthAndNotifiesVisualizer() {
        final Hero hero = new Hero(0, 0);
        final Enemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 1, 0);
        enemy.setAttackingPower(18);

        controller.onHeroHit(hero, enemy, enemy.getAttackingPower());

        assertEquals(82, hero.getHealth());
        assertEquals(1, visualizer.heroHitCount);
        assertEquals(0, visualizer.heroDefeatedCount);
    }

    @Test
    void heroHitByWeaponCanBeDefeatedWithoutNegativeHealth() {
        final Hero hero = new Hero(0, 0);
        hero.decreaseHealth(95); // leave hero with 5 health

        final IWeapon weapon = new WeaponStub(20);

        controller.onHeroHit(hero, weapon, weapon.getAttackingDamage());

        assertEquals(0, hero.getHealth());
        assertEquals(0, visualizer.heroHitCount);
        assertEquals(1, visualizer.heroDefeatedCount);
    }

    @Test
    void heroDamageIsClampedAtZeroWhenDamageExceedsHealth() {
        final Hero hero = new Hero(0, 0);
        final Enemy enemy = new Enemy(TileType.ENEMY_LR.getType(), 1, 0);

        controller.onHeroHit(hero, enemy, 150);

        assertEquals(0, hero.getHealth());
        assertEquals(1, visualizer.heroDefeatedCount);
    }

    private static class TestGameWorldVisualizer extends GameWorldVisualizer {
        private int heroHitCount;
        private int heroDefeatedCount;

        @Override
        public void handleHeroHit() {
            heroHitCount++;
        }

        @Override
        public void handleHeroDefeated() {
            heroDefeatedCount++;
        }
    }

    private static class WeaponStub implements IWeapon {
        private final int type = TileType.WEAPON_SWORD.getType();
        private final String name = "Test Weapon";
        private int damage;
        private int range = 1;

        private WeaponStub(int damage) {
            this.damage = damage;
        }

        @Override
        public int getAttackingDamage() {
            return damage;
        }

        @Override
        public void setAttackingDamage(int attackingDamage) {
            this.damage = attackingDamage;
        }

        @Override
        public int getRange() {
            return range;
        }

        @Override
        public void setRange(int range) {
            this.range = range;
        }

        @Override
        public boolean canAttackBackward() {
            return false;
        }

        @Override
        public boolean canAttackInAllDirections() {
            return false;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return name;
        }
    }
}
