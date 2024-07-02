package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public interface IMovableTileElement extends ITileMapElement {

    void setX(int x);
    void setY(int y);
    void setMoveDirection(MoveDirection moveDirection);
    Optional<MoveDirection> getMoveDirection();

    void setMovementStrategy(IMovementStrategy movementStrategy);
    IMovementStrategy getMovementStrategy();
}
