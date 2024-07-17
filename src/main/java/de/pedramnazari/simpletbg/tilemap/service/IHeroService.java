package de.pedramnazari.simpletbg.tilemap.service;

import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;

public interface IHeroService {

    IHero getHero();

    MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext);

    void init(int heroStartX, int heroStartY);

    List<Point> heroAttacks(Collection<IEnemy> enemies);

    List<Point> heroAttacksUsingWeapon(IWeapon weapon, IHero hero, Collection<IEnemy> enemies);

    // TODO: Remove this method
    CollisionDetectionService getCollisionDetectionService();
}
