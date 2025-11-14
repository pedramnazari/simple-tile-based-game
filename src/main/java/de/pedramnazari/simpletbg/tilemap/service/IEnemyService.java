package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;

public interface IEnemyService {
    void init(Collection<IEnemy> enemies);

    void reset(Collection<IEnemy> enemies);

    List<MovementResult> moveEnemies(GameContext gameContext);

    Collection<IEnemy> getEnemies();

    boolean isInitialized();
}
