package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.character.hero.service.HeroAttackService;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.IHero;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;

public interface IHeroService {

    IHero getHero();

    MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext);

    void init(int heroStartX, int heroStartY);

    HeroAttackService getHeroAttackService();

    List<Point> heroAttacks(Collection<IEnemy> enemies);

    // TODO: Remove this method
    CollisionDetectionService getCollisionDetectionService();
}
