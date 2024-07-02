package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.MoveDirection;

public class TopToBottomMovementStrategy extends OneDimensionalMovementStrategy {

    public TopToBottomMovementStrategy(CollisionDetectionService collisionDetectionService) {
        super(MoveDirection.UP, MoveDirection.DOWN, collisionDetectionService);
    }

}
