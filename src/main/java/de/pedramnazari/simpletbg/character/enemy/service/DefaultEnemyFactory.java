package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.character.enemy.model.IEnemyFactory;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CircularMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.service.navigation.CollisionDetectionService;
import de.pedramnazari.simpletbg.tilemap.service.navigation.LeftToRightMovementStrategy;
import de.pedramnazari.simpletbg.tilemap.service.navigation.TopToBottomMovementStrategy;

public class DefaultEnemyFactory extends AbstractTileMapElementFactory<Enemy> implements IEnemyFactory {

    private final CollisionDetectionService collisionDetectionService;

    public DefaultEnemyFactory(CollisionDetectionService collisionDetectionService) {
        this.collisionDetectionService = collisionDetectionService;
    }

    @Override
    protected Enemy createNonEmptyElement(int type, int x, int y) {
        final Enemy enemy;
        if (type == TileType.ENEMY_LR.getType()) {
            enemy = new Enemy(TileType.ENEMY_LR.getType(), x, y);
            enemy.setMovementStrategy(new LeftToRightMovementStrategy(collisionDetectionService));
            System.out.println("Created enemy at " + x + ", " + y);
        }
        else if (type == TileType.ENEMY_TD.getType()) {
            enemy = new Enemy(TileType.ENEMY_TD.getType(), x, y);
            enemy.setMovementStrategy(new TopToBottomMovementStrategy(collisionDetectionService));
            System.out.println("Created enemy at " + x + ", " + y);
        }
        else if (type == TileType.ENEMY_2D.getType()) {
            enemy = new Enemy(TileType.ENEMY_2D.getType(), x, y);
            enemy.setMovementStrategy(new CircularMovementStrategy(collisionDetectionService));
            System.out.println("Created enemy at " + x + ", " + y);
        }
        else {
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        return enemy;
    }

}
