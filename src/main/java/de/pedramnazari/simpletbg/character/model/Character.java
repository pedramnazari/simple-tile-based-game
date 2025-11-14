package de.pedramnazari.simpletbg.character.model;

import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;

import java.util.Optional;

public abstract class Character implements ICharacter {

    private final int type;
    private int x;
    private int y;
    private int health = 100;
    private int attackingPower = 10;
    private MoveDirection moveDirection;
    private IMovementStrategy movementStrategy;


    protected Character(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
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
    public int getType() {
        return type;
    }


    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Health must be positive");
        }

        this.health = Math.min(100, health);
    }

    @Override
    public int increaseHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Health must be positive");
        }

        this.health = Math.min(100, this.health + health);

        return getHealth();
    }

    @Override
    public int decreaseHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Health must be positive");
        }

        this.health = Math.max(0, this.health - health);

        return getHealth();
    }

    @Override
    public void setAttackingPower(int attackingPower) {
        this.attackingPower = attackingPower;
    }

    @Override
    public int getAttackingPower() {
        return attackingPower;
    }
}
