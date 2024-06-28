package de.pedramnazari.simpletbg.model;

import java.util.Optional;

public interface IMoveableTileElement extends ITileMapElement {

    void setX(int x);
    void setY(int y);
    void setMoveDirection(MoveDirection moveDirection);
    Optional<MoveDirection> getMoveDirection();
}
