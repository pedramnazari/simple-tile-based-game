package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IHeroService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;

public class HeroServiceMock implements IHeroService {

    private final IHero hero;

    public HeroServiceMock(IHero hero) {
        this.hero = hero;
    }

    public HeroServiceMock() {
        this(new Hero(0, 0));
    }

    @Override
    public IHero getHero() {
        return this.hero;
    }

    @Override
    public MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext) {
        return null;
    }

    @Override
    public void init(int heroStartX, int heroStartY) {

    }

    @Override
    public List<Point> heroAttacks(Collection<IEnemy> enemies) {
        return List.of();
    }

    @Override
    public List<Point> heroAttacksUsingWeapon(IWeapon weapon, IHero hero, Collection<IEnemy> enemies) {
        return List.of();
    }

    @Override
    public CollisionDetectionService getCollisionDetectionService() {
        return null;
    }

    @Override
    public void useItem(IItem item) {

    }
}
