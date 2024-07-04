package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;

import java.util.ArrayList;
import java.util.List;

public class EnemyHitNotifier {
    private final List<IEnemyHitListener> enemyHitListener = new ArrayList<>();

    public void addListener(IEnemyHitListener listener) {
        enemyHitListener.add(listener);
    }

    public void notifyEnemyHit(Enemy enemy, int damage) {
        for (IEnemyHitListener listener : enemyHitListener) {
            listener.onEnemyHit(enemy, damage);
        }
    }

    public void notifyEnemyDefeated(Enemy enemy) {
        for (IEnemyHitListener listener : enemyHitListener) {
            listener.onEnemyDefeated(enemy);
        }
    }

    public void notifyAllEnemiesDefeated() {
        for (IEnemyHitListener listener : enemyHitListener) {
            listener.onAllEnemiesDefeated();
        }
    }
}
