package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.model.IHeroFactory;
import de.pedramnazari.simpletbg.inventory.model.IItemCollector;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpNotifier;
import de.pedramnazari.simpletbg.inventory.service.ItemPickUpNotifier;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

public class HeroService implements IItemPickUpNotifier {

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();

    private final IHeroFactory heroFactory;
    private final HeroMovementService heroMovementService;
    private Hero hero;

    public HeroService(IHeroFactory heroFactory, HeroMovementService heroMovementService) {
        this.heroFactory = heroFactory;
        this.heroMovementService = heroMovementService;
    }

    public MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext) {
        final MovementResult result =  heroMovementService.moveElement(hero, moveDirection, gameContext);

        handleItemIfCollected(result);

        return result;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final Item item = result.getCollectedItem().get();
            hero.getInventory().addItem(item);

            itemPickUpNotifier.notifyItemPickedUp(hero, item);
        }
    }

    @Override
    public void addItemPickupListener(IItemPickUpListener itemPickUpListener) {
        itemPickUpNotifier.addItemPickupListener(itemPickUpListener);
    }

    @Override
    public void notifyItemPickedUp(IItemCollector element, Item item) {
        itemPickUpNotifier.notifyItemPickedUp(element, item);
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
}
