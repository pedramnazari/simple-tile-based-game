package de.pedramnazari.simpletbg.inventory.model.projectile;

import de.pedramnazari.simpletbg.tilemap.model.IProjectile;
import de.pedramnazari.simpletbg.tilemap.model.IWeapon;
import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;
import de.pedramnazari.simpletbg.tilemap.model.TileType;

import java.util.Optional;

public class WindProjectile implements IProjectile {

    private final int damage;
    private int x;
    private int y;
    private MoveDirection moveDirection;
    private IMovementStrategy movementStrategy;
    private int remainingRange;
    private boolean active = true;

    public WindProjectile(int x, int y, MoveDirection direction, int range, int damage) {
        this.x = x;
        this.y = y;
        this.moveDirection = direction;
        this.remainingRange = range;
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
        return null; // Companion attacks don't have a weapon
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
        return TileType.PROJECTILE_WIND.getType();
    }
}
