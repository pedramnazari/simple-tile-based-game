package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMovementStrategy;

public abstract class AbstractMovementStrategy implements IMovementStrategy {

    private final CollisionDetectionService collisionDetectionService;

    public AbstractMovementStrategy(CollisionDetectionService collisionDetectionService) {
        this.collisionDetectionService = collisionDetectionService;
    }

    @Override
    public CollisionDetectionService getCollisionDetectionService() {
        return collisionDetectionService;
    }

}
