package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface ICharacter extends IMovableTileElement {
    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    @Override
    void setMoveDirection(MoveDirection moveDirection);

    Optional<MoveDirection> getMoveDirection();

    @Override
    void setMovementStrategy(IMovementStrategy movementStrategy);

    @Override
    IMovementStrategy getMovementStrategy();

    @Override
    int getType();

    int getHealth();

    int increaseHealth(int health);
    int decreaseHealth(int health);

    void setAttackingPower(int attackingPower);

    int getAttackingPower();
}
