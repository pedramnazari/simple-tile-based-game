package de.pedramnazari.simpletbg.inventory.model.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.IProjectileEffect;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.Optional;

public class IceProjectile implements IProjectile {

    private final String name = "Ice Projectile";
    private final String description = "A freezing projectile shot from an ice wand.";
    private final IWeapon weapon;
    private final int damage;
    private final IProjectileEffect effect = new FreezeEffect();

    private int x;
    private int y;
    private MoveDirection moveDirection;
    private IMovementStrategy movementStrategy;
    private int remainingRange;
    private boolean active = true;

    public IceProjectile(int x,
                         int y,
                         MoveDirection direction,
                         int range,
                         IWeapon weapon,
                         int damage) {
        this.x = x;
        this.y = y;
        this.moveDirection = direction;
        this.remainingRange = range;
        this.weapon = weapon;
        this.damage = damage;
    }

    @Override
    public int getRemainingRange() {
        return remainingRange;
    }

    @Override
    public void setRemainingRange(int remainingRange) {
        this.remainingRange = Math.max(0, remainingRange);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void deactivate() {
        this.active = false;
    }

    @Override
    public IWeapon getWeapon() {
        return weapon;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    @Override
    public Optional<MoveDirection> getMoveDirection() {
        return Optional.ofNullable(moveDirection);
    }

    @Override
    public void setMovementStrategy(IMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    @Override
    public IMovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getType() {
        return TileType.PROJECTILE_ICE.getType();
    }

    @Override
    public Optional<IProjectileEffect> getEffect() {
        return Optional.of(effect);
    }
}
