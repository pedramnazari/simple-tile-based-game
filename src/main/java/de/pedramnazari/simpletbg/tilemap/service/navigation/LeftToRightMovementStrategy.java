package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;

public class LeftToRightMovementStrategy extends OneDimensionalMovementStrategy {

    public LeftToRightMovementStrategy(CollisionDetectionService collisionDetectionService) {
        super(MoveDirection.LEFT, MoveDirection.RIGHT, collisionDetectionService);
    }

}
