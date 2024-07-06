package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.hero.model.Hero;
import de.pedramnazari.simpletbg.character.hero.model.IHeroFactory;
import de.pedramnazari.simpletbg.character.model.Character;
import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.model.Weapon;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpNotifier;
import de.pedramnazari.simpletbg.inventory.service.ItemPickUpNotifier;
import de.pedramnazari.simpletbg.service.GameContext;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.Point;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;

public class HeroService implements IItemPickUpNotifier, IHeroAttackNotifier {

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();

    private final IHeroFactory heroFactory;
    private final HeroMovementService heroMovementService;
    private final HeroAttackService heroAttackService;
    private Hero hero;

    public HeroService(IHeroFactory heroFactory, HeroMovementService heroMovementService, HeroAttackService heroAttackService) {
        this.heroFactory = heroFactory;
        this.heroMovementService = heroMovementService;
        this.heroAttackService = heroAttackService;
    }

    public MovementResult moveHero(MoveDirection moveDirection, GameContext gameContext) {
        final MovementResult result =  heroMovementService.moveElement(hero, moveDirection, gameContext);

        handleItemIfCollected(result);

        return result;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final Item item = result.getCollectedItem().get();

            if (item instanceof Weapon weapon) {
                hero.setWeapon(weapon);
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
    public void notifyItemPickedUp(Character element, Item item) {
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

    @Override
    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackService.addHeroAttackListener(listener);
    }

    @Override
    public void notifyHeroAttacksCharacter(Character attackedCharacter, int damage) {
        heroAttackService.notifyHeroAttacksCharacter(attackedCharacter, damage);
    }

    public List<Point> heroAttacks(Collection<Enemy> enemies) {
        return heroAttackService.heroAttacks(hero, enemies);
    }
}
