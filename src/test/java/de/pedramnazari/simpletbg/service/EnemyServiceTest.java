package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Enemy;
import de.pedramnazari.simpletbg.model.IEnemyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnemyServiceTest {

    private EnemyService enemyService;

    @BeforeEach
    public void setUp() {
        IEnemyFactory enemyFactory = new DefaultEnemyFactory();

        enemyService = new EnemyService(enemyFactory);
    }

    @Test
    public void testCreateEnemies() {
        int[][] map = new int[][] {
                {0, 0, 1},
                {1, 0, 0},
                {0, 1, 0},
        };

        final Collection<Enemy> enemies = enemyService.createEnemies(map);
        assertNotNull(enemies);
        assertEquals(3, enemies.size());

        final Enemy aEnemy = enemies.stream().filter(e -> e.getX() == 2 && e.getY() == 0).findFirst().orElse(null);
        assertNotNull(aEnemy);

        final Enemy bEnemy = enemies.stream().filter(e -> e.getX() == 0 && e.getY() == 1).findFirst().orElse(null);
        assertNotNull(bEnemy);

        final Enemy cEnemy = enemies.stream().filter(e -> e.getX() == 1 && e.getY() == 2).findFirst().orElse(null);
        assertNotNull(cEnemy);
    }




}
