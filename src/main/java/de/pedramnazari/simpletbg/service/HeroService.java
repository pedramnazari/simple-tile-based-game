package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.*;

import java.util.Collection;

public class HeroService {

    private final IHeroFactory heroFactory;
    private final HeroMovementService heroMovementService;
    private Hero hero;

    public HeroService(IHeroFactory heroFactory, HeroMovementService heroMovementService) {
        this.heroFactory = heroFactory;
        this.heroMovementService = heroMovementService;
    }

  public MovementResult moveHero(TileMap tileMap, Collection<Item> items, MoveDirection moveDirection, MapNavigator mapNavigator, String currentMapIndex) {
        return heroMovementService.moveElement(tileMap, items, this.hero, moveDirection, mapNavigator, currentMapIndex);
    }

    public void init(int heroStartX, int heroStartY) {
        if (hero != null) {
            throw new IllegalStateException("Hero already initialized");
        }

        hero = heroFactory.createElement(Hero.HERO_TYPE, heroStartX, heroStartY);
        // TODO: inject inventory or use factory
        hero.setInventory(new Inventory());
    }

    public Hero getHero() {
        return hero;
    }

    public HeroMovementService getHeroMovementService() {
        return heroMovementService;
    }
}
