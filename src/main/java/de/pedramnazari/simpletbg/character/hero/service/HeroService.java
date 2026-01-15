package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.model.bomb.BombPlacer;
import de.pedramnazari.simpletbg.inventory.service.event.*;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class HeroService implements IHeroService, IHeroProvider, IHeroAttackNotifier, ICharacterMovedToSpecialTileListener {

    private static final Logger logger = Logger.getLogger(HeroService.class.getName());

    private final HeroMovedNotifier heroMovedNotifier = new HeroMovedNotifier();

    private final Collection<IItemEventListener> itemEventListeners = new ArrayList<>();

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

        if (result.hasElementMoved()) {
            handleItemIfCollected(result);
            notifyHeroMoved(hero, result.getOldX(), result.getOldY());
        }

        return result;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final IItem item = result.getCollectedItem().get();

            if (item instanceof IWeapon weapon) {
                handleWeaponCollected(weapon);
            }
            else if (item instanceof IRing ring) {
                handleRingCollected(ring);
            }
            else if (item instanceof IArmor armor) {
                handleArmorCollected(armor);
            }
            else if (item instanceof IConsumableItem magicPotion) {
                handleConsumableItemCollected(magicPotion);
            }
            else {
                addItemToInventory(item);
            }

            notifyItemCollected(new ItemCollectedEvent(hero, item));
        }
    }

    private void handleWeaponCollected(IWeapon weapon) {
        if (weapon instanceof BombPlacer newBombPlacer
                && hero.getWeapon().isPresent()
                && hero.getWeapon().get() instanceof BombPlacer oldBombPlacer) {
            oldBombPlacer.setRange(oldBombPlacer.getRange() + newBombPlacer.getRange());
            logger.info("Merging bomb placers. New Range is " + oldBombPlacer.getRange());
        }
        else if (hero.getWeapon().isEmpty()) {
            hero.setWeapon(weapon);
        }
        else { // hero already has a weapon
            addItemToInventory(weapon);
        }
    }

    private void handleRingCollected(IRing ring) {
        if (hero.getRing().isEmpty()) {
            hero.setRing(ring);
        }
        else {
            addItemToInventory(ring);
        }
    }

    private void handleArmorCollected(IArmor armor) {
        if (hero.getArmor().isEmpty()) {
            hero.setArmor(armor);
        }
        else {
            addItemToInventory(armor);
        }
    }

    private void handleConsumableItemCollected(IConsumableItem magicPotion) {
        logger.info("Hero picked up magic potion");
        consumeItem(magicPotion);
    }

    private void addItemToInventory(IItem item) {
        hero.getInventory().addItem(item);
    }

    public void addHeroMovedListener(IHeroMovedListener listener) {
        heroMovedNotifier.addListener(listener);
    }

    public void removeListener(IHeroMovedListener listener) {
        heroMovedNotifier.removeListener(listener);
    }

    public void notifyHeroMoved(IHero hero, int oldX, int oldY) {
        heroMovedNotifier.notifyHeroMoved(hero, oldX, oldY);
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
    public void addHeroAttackListener(IHeroAttackListener listener) {
        heroAttackService.addHeroAttackListener(listener);
    }

    @Override
    public void notifyHeroAttackCharacter(ICharacter attackedCharacter, int damage) {
        heroAttackService.notifyHeroAttackCharacter(attackedCharacter, damage);
    }

    @Override
    public List<Point> heroAttacks(Collection<IEnemy> enemies) {
        return heroAttackService.heroAttacks(hero, enemies);
    }

    @Override
    public List<Point> heroAttacksUsingWeapon(IWeapon weapon, IHero hero, Collection<IEnemy> enemies) {
        return heroAttackService.heroAttacksUsingWeapon(weapon, hero, enemies);
    }


    // TODO: Remove this method
    @Override
    public CollisionDetectionService getCollisionDetectionService() {
        return heroMovementService.getCollisionDetectionService();
    }

    @Override
    public void useItem(IItem item) {
        if (item instanceof IConsumableItem consumableItem) {
            consumeItem(consumableItem);
        }
        else if (item instanceof IWeapon newWeapon) {
            useWeapon(newWeapon);
        }
        else if (item instanceof IRing newRing) {
            useRing(newRing);
        }
        else {
            throw new IllegalArgumentException("Item type not supported: " + item.getClass());
        }
    }

    private void useRing(IRing newRing) {
        final IRing oldRing = hero.getRing().orElse(null);

        if (oldRing != null) {
            addItemToInventory(oldRing);
        }

        hero.setRing(newRing);
    }

    private void useWeapon(IWeapon newWeapon) {
        final IWeapon oldWeapon = hero.getWeapon().orElse(null);

        if (oldWeapon != null) {
            addItemToInventory(oldWeapon);
        }

        hero.setWeapon(newWeapon);
    }

    private void consumeItem(IConsumableItem consumableItem) {
        consumableItem.consume(hero);
    }

    @Override
    public void onCharacterMovedToSpecialTile(ICharacter character, Tile specialTile) {
        if (!hero.equals(character)) {
            return;
        }

        if (specialTile.getType() == TileType.EXIT.getType()) {
            logger.info("Hero reached the exit");
        }
        else if (specialTile.isPortal()) {
            logger.info("Hero stepped on a portal");

            if(specialTile.getPortalDestination().isPresent()) {
                final Tile destination = specialTile.getPortalDestination().get();
                logger.info("Hero will be moved to portal destination: " + destination);
                final MovementResult result = heroMovementService.moveElementToPositionWithinMap(GameContext.getInstance(), hero, destination.getX(), destination.getY());
            }
            else {
                throw new IllegalStateException("Portal destination not set");
            }
        }
    }

    public void addItemEventListener(IItemEventListener listener) {
        itemEventListeners.add(listener);
    }

    private void notifyItemCollected(ItemCollectedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemCollected(event);
        }
    }

    private void notifyItemEquipped(ItemEquippedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemEquipped(event);
        }
    }

    private void notifyItemAddedToInventory(ItemAddedToInventoryEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemAddedToInventory(event);
        }
    }

    private void notifyItemConsumed(ItemConsumedEvent event) {
        for (IItemEventListener listener : itemEventListeners) {
            listener.onItemUsed(event);
        }
    }
}
