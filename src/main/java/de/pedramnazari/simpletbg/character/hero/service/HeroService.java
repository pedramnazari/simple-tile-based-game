package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpNotifier;
import de.pedramnazari.simpletbg.inventory.service.ItemPickUpNotifier;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.service.IHeroService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;

public class HeroService implements IHeroService, IHeroProvider, IItemPickUpNotifier, IHeroAttackNotifier {

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();

    private final IHeroFactory heroFactory;
    private final HeroMovementService heroMovementService;
    private final HeroAttackService heroAttackService;
    private IHero hero;

    public HeroService(IHeroFactory heroFactory, HeroMovementService heroMovementService, HeroAttackService heroAttackService) {
        this.heroFactory = heroFactory;
        this.heroMovementService = heroMovementService;
        this.heroAttackService = heroAttackService;
    }

    @Override
    public MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext) {
        final MovementResult result = heroMovementService.moveElement(hero, moveDirection, gameContext);

        handleItemIfCollected(result);

        return result;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final IItem item = result.getCollectedItem().get();

            if (item instanceof IWeapon weapon) {
                hero.setWeapon(weapon);
            }
            else if (item instanceof IRing ring) {
                hero.setRing(ring);
            }
            else {
                hero.getInventory().addItem(item);
            }

            itemPickUpNotifier.notifyItemPickedUp(hero, item);
        }
    }

    @Override
    public void addItemPickupListener(IItemPickUpListener itemPickUpListener) {
        itemPickUpNotifier.addItemPickupListener(itemPickUpListener);
    }

    @Override
    public void notifyItemPickedUp(ICharacter element, IItem item) {
        itemPickUpNotifier.notifyItemPickedUp(element, item);
    }


    @Override
    public void init(int heroStartX, int heroStartY) {
        if (hero != null) {
            throw new IllegalStateException("Hero already initialized");
        }

        hero = heroFactory.createElement(IHero.HERO_TYPE, heroStartX, heroStartY);
        // TODO: inject inventory or use factory
        hero.setInventory(new Inventory());
    }

    @Override
    public IHero getHero() {
        return hero;
    }

    @Override
    public IHero getCharacter() {
        return getHero();
    }

    @Override
    public HeroAttackService getHeroAttackService() {
        return heroAttackService;
    }

    @Override
    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackService.addHeroAttackListener(listener);
    }

    @Override
    public void notifyHeroAttacksCharacter(ICharacter attackedCharacter, int damage) {
        heroAttackService.notifyHeroAttacksCharacter(attackedCharacter, damage);
    }

    @Override
    public List<Point> heroAttacks(Collection<IEnemy> enemies) {
        return heroAttackService.heroAttacks(hero, enemies);
    }

    // TODO: Remove this method
    @Override
    public CollisionDetectionService getCollisionDetectionService() {
        return heroMovementService.getCollisionDetectionService();
    }

}
