package de.pedramnazari.simpletbg.character.model;

import de.pedramnazari.simpletbg.tilemap.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;

import java.util.Optional;

public abstract class Character implements IMoveableTileElement {

    private int x;
    private int y;
    private MoveDirection moveDirection;


    protected Character(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected Character() {
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





}
