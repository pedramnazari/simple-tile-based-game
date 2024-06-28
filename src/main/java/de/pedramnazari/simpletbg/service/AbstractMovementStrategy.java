package de.pedramnazari.simpletbg.service;

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
