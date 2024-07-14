package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.service.EnemyService;

import java.util.ArrayList;
import java.util.List;

public class EnemyServiceMock extends EnemyService {

    private final List<Enemy> enemies = new ArrayList<>();


    public EnemyServiceMock() {
        super(null);
    }

    public EnemyServiceMock(List<Enemy> enemies) {
        this();
        this.enemies.addAll(enemies);
    }
}
