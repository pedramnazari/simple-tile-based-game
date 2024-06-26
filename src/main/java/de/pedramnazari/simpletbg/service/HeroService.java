package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.Collection;

public class HeroService {

    private final IHeroFactory heroFactory;
    private final HeroMovementService heroMovementService;

    public HeroService(IHeroFactory heroFactory, HeroMovementService heroMovementService) {
        this.heroFactory = heroFactory;
        this.heroMovementService = heroMovementService;
    }


    public MovementResult moveTileMapElement(TileMap tileMap, Collection<Item> items, Hero hero, MoveDirection moveDirection, MapNavigator mapNavigator, String currentMapIndex) {
        return heroMovementService.moveTileMapElement(tileMap, items, hero, moveDirection, mapNavigator, currentMapIndex);
    }

    public Hero createHero(int x, int y) {
        final Hero hero = heroFactory.createElement(Hero.HERO_TYPE, x, y);
        // TODO: inject inventory or use factory
        hero.setInventory(new Inventory());

        return hero;
    }
}
