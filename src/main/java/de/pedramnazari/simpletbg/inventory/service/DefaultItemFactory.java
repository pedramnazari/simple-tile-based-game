package de.pedramnazari.simpletbg.inventory.service;

import de.pedramnazari.simpletbg.inventory.model.ConsumableItem;
import de.pedramnazari.simpletbg.inventory.model.FireStaff;
import de.pedramnazari.simpletbg.inventory.model.Item;
import de.pedramnazari.simpletbg.inventory.model.Ring;
import de.pedramnazari.simpletbg.inventory.model.Weapon;
import de.pedramnazari.simpletbg.inventory.model.bomb.BombPlacer;
import de.pedramnazari.simpletbg.inventory.model.bomb.IBombService;
import de.pedramnazari.simpletbg.inventory.service.magiceffect.HealthModifierMagicEffect;
import de.pedramnazari.simpletbg.tilemap.model.*;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;

import java.util.Optional;

public class DefaultItemFactory extends AbstractTileMapElementFactory<IItem> implements IItemFactory {

    public static final String ITEM_MAGIC_YELLOW_KEY_NAME = "Magic Yellow Key";
    public static final String ITEM_MAGIC_YELLOW_KEY_DESC = "A yellow key that opens the door to the next level.";

    public static final String ITEM_MAGIC_YELLOW_KEY2_NAME = "Magic Yellow Key";
    public static final String ITEM_MAGIC_YELLOW_KEY2_DESC = "A yellow key that opens the door to the next level.";

    private IBombService bombService;
    private IBombFactory bombFactory;
    private IProjectileFactory projectileFactory;

    @Override
    protected IItem createNonEmptyElement(int type, int x, int y) {
        IItem item;
        String itemName;
        String itemDescription;

        // TODO: use weapon factory for weapon type

        if (type == TileType.ITEM_YELLOW_KEY.getType()) {
            itemName = ITEM_MAGIC_YELLOW_KEY_NAME;
            itemDescription = ITEM_MAGIC_YELLOW_KEY_DESC;
            item = new Item(x, y, itemName, itemDescription, type);
        }
        else if (type == TileType.ITEM_YELLOW_KEY2.getType()) {
            itemName = ITEM_MAGIC_YELLOW_KEY2_NAME;
            itemDescription = ITEM_MAGIC_YELLOW_KEY2_DESC;
            item = new Item(x, y, itemName, itemDescription, type);
        }
        else if (type == TileType.HEALTH_POTION.getType()) {
            final IMagicEffect magicPower = new HealthModifierMagicEffect(20);
            final ConsumableItem magicPotion = new ConsumableItem(magicPower, x, y, type);

            item = magicPotion;
        }
        else if (type == TileType.POISON_POTION.getType()) {
        final IMagicEffect magicPower = new HealthModifierMagicEffect(-20);
        final ConsumableItem poisonPotion = new ConsumableItem(magicPower, x, y, type);

        item = poisonPotion;
        }
        else if (type == TileType.WEAPON_SWORD.getType()) {
            itemName = "Sword";
            itemDescription = "A sword that can be used to fight enemies.";
            final Weapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(20);

            item = weapon;
        }
        else if (type == TileType.WEAPON_SWORD2.getType()) {
            itemName = "Black Sword";
            itemDescription = "A strong black sword that can be used to fight enemies.";
            final Weapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(100);

            item = weapon;
        }
        else if (type == TileType.WEAPON_LANCE.getType()) {
            itemName = "Lance";
            itemDescription = "A lance that can be used to fight enemies.";
            final Weapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(10);
            weapon.setRange(2);

            item = weapon;
        }
        else if (type == TileType.WEAPON_DOUBLE_ENDED_LANCE.getType()) {
            itemName = "Double-Ended Lance";
            itemDescription = "A double-ended lance that can be used to fight enemies.";
            final Weapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(20);
            weapon.setRange(2);
            weapon.setCanAttackBackward(true);

            item = weapon;
        }
        else if (type == TileType.WEAPON_MULTI_SPIKE_LANCE.getType()) {
            itemName = "Multi-Spike Lance";
            itemDescription = "A multi-spike lance that can be used to fight enemies in all directions.";
            final Weapon weapon = new Weapon(x, y, itemName, itemDescription, type);
            weapon.setAttackingDamage(15);
            weapon.setRange(2);
            weapon.setCanAttackInAllDirections(true);

            item = weapon;
        }
        else if (type == TileType.RING_MAGIC1.getType()) {
            itemName = "Attacking Power Ring";
            itemDescription = "A magic ring that increases the attacking power of the hero.";
            IRing ring = new Ring(x, y, itemName, itemDescription, type);
            ring.setAttackingPower(20);

            item = ring;
        }
        else if (type == TileType.WEAPON_BOMB_PLACER.getType()) {
            itemName = "Bomb Placer";
            itemDescription = "A bomb placer that can be used to place bombs.";
            final BombPlacer bombPlacer = new BombPlacer(bombFactory, bombService, x, y);
            bombPlacer.setRange(1);

            item = bombPlacer;

        }
        else if (type == TileType.WEAPON_FIRE_STAFF.getType()) {
            itemName = "Fire Staff";
            itemDescription = "A staff that shoots fire over long distances.";
            if (projectileFactory == null) {
                throw new IllegalStateException("Projectile factory is not configured for Fire Staff creation");
            }
            final FireStaff fireStaff = new FireStaff(x, y, projectileFactory);
            item = fireStaff;

        }
        else {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }

        return item;
    }

    public void setBombService(IBombService bombService) {
        this.bombService = bombService;
    }

    public Optional<IBombService> getBombService() {
        return Optional.ofNullable(bombService);
    }

    public void setBombFactory(IBombFactory bombFactory) {
        this.bombFactory = bombFactory;
    }

    public Optional<IBombFactory> getBombFactory() {
        return Optional.ofNullable(bombFactory);
    }

    public void setProjectileFactory(IProjectileFactory projectileFactory) {
        this.projectileFactory = projectileFactory;
    }
}
