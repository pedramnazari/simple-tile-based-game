package de.pedramnazari.simpletbg.character.hero.service;

import de.pedramnazari.simpletbg.character.service.IHeroAttackListener;
import de.pedramnazari.simpletbg.inventory.model.Inventory;
import de.pedramnazari.simpletbg.inventory.model.bomb.BombPlacer;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.inventory.service.IItemPickUpNotifier;
import de.pedramnazari.simpletbg.inventory.service.ItemPickUpNotifier;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.*;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.MovementResult;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class HeroService implements IHeroService, IHeroProvider, IItemPickUpNotifier, IHeroAttackNotifier, ICharacterMovedToSpecialTileListener {

    private static final Logger logger = Logger.getLogger(HeroService.class.getName());

    private final ItemPickUpNotifier itemPickUpNotifier = new ItemPickUpNotifier();
    private final HeroMovedNotifier heroMovedNotifier = new HeroMovedNotifier();

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

        if (result.hasElementMoved()) {
            notifyHeroMoved(hero, result.getOldX(), result.getOldY());
        }

        return result;
    }

    private void handleItemIfCollected(MovementResult result) {
        if (result.getCollectedItem().isPresent()) {
            final IItem item = result.getCollectedItem().get();

            if (item instanceof IWeapon weapon) {
                if (weapon instanceof BombPlacer newBombPlacer
                        && hero.getWeapon().isPresent()
                        && hero.getWeapon().get() instanceof BombPlacer oldBombPlacer) {
                    oldBombPlacer.setRange(oldBombPlacer.getRange() + newBombPlacer.getRange());
                    logger.info("Merging bomb placers. New Range is " + oldBombPlacer.getRange());
                }
                else {
                    hero.setWeapon(weapon);
                }
            }
            else if (item instanceof IRing ring) {
                hero.setRing(ring);
            }
            else if (item instanceof IConsumableItem magicPotion) {
                logger.info("Hero picked up magic potion");
                magicPotion.consume(hero);
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

                if (result.hasElementMoved()) {
                    logger.info("!!!Hero moved to new position: " + hero.getX() + ", " + hero.getY());
                }
                else {
                    logger.info("???Hero did not move");
                }

                logger.info("Hero's new position: " + hero.getX() + ", " + hero.getY());
            }
            else {
                throw new IllegalStateException("Portal destination not set");
            }
        }
    }
}
