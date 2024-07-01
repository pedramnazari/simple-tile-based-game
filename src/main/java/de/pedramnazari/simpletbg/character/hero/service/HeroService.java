package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.model.IHeroFactory;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.model.MoveDirection;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.service.MovementResult;

public class HeroService {

    private final IHeroFactory heroFactory;
    private final HeroMovementService heroMovementService;
    private Hero hero;

    public HeroService(IHeroFactory heroFactory, HeroMovementService heroMovementService) {
        this.heroFactory = heroFactory;
        this.heroMovementService = heroMovementService;
    }

    public MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext) {
        return heroMovementService.moveElement(hero, moveDirection, gameContext);
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
