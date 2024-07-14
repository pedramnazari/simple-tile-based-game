package de.pedramnazari.simpletbg.tilemap.model;

import java.util.Optional;

public enum MoveDirection {
    UP, DOWN, LEFT, RIGHT;

    public static Optional<MoveDirection> getDirection(int x, int y, int newX, int newY) {
        MoveDirection direction;

        if (newX > x) {
            direction = RIGHT;
        } else if (newX < x) {
            direction = LEFT;
        } else if (newY > y) {
            direction = DOWN;
        } else if (newY < y){
            direction = UP;
        }
        else {
            direction = null;
        }

        return Optional.ofNullable(direction);
    }

    public static MoveDirection getOppositeDirection(MoveDirection direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> throw new IllegalArgumentException("Unexpected value: " + direction);
        };
    }
}
