package de.pedramnazari.simpletbg.character.model;

import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;

import java.util.Optional;

public abstract class Character implements IMovableTileElement {

    private final int type;
    private int x;
    private int y;
    private int health = 100;
    private MoveDirection moveDirection;
    private IMovementStrategy movementStrategy;


    protected Character(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
