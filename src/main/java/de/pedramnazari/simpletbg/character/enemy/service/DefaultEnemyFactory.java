package de.pedramnazari.simpletbg.character.enemy.service;

import de.pedramnazari.simpletbg.character.enemy.model.Enemy;
import de.pedramnazari.simpletbg.tilemap.model.ICharacter;
import de.pedramnazari.simpletbg.tilemap.model.ICharacterProvider;
import de.pedramnazari.simpletbg.tilemap.model.IEnemy;
import de.pedramnazari.simpletbg.tilemap.model.TileType;
import de.pedramnazari.simpletbg.tilemap.service.AbstractTileMapElementFactory;
import de.pedramnazari.simpletbg.tilemap.service.navigation.*;

public class DefaultEnemyFactory extends AbstractTileMapElementFactory<IEnemy> implements IEnemyFactory {

    private final CollisionDetectionService collisionDetectionService;
    private final ICharacterProvider<? extends ICharacter> characterProvider;

    public DefaultEnemyFactory(CollisionDetectionService collisionDetectionService, ICharacterProvider<? extends ICharacter> characterProvider) {
        this.collisionDetectionService = collisionDetectionService;
        this.characterProvider = characterProvider;
    }

    @Override
    protected IEnemy createNonEmptyElement(int type, int x, int y) {
        final IEnemy enemy;
        if (type == TileType.ENEMY_LR.getType()) {
            enemy = new Enemy(TileType.ENEMY_LR.getType(), x, y);
            enemy.setMovementStrategy(new LeftToRightMovementStrategy(collisionDetectionService));
        }
        else if (type == TileType.ENEMY_TD.getType()) {
            enemy = new Enemy(TileType.ENEMY_TD.getType(), x, y);
            enemy.setMovementStrategy(new TopToBottomMovementStrategy(collisionDetectionService));
        }
        else if (type == TileType.ENEMY_2D.getType()) {
            enemy = new Enemy(TileType.ENEMY_2D.getType(), x, y);
            enemy.setMovementStrategy(new CircularMovementStrategy(collisionDetectionService));
        }
        else if (type == TileType.ENEMY_FH.getType()) {
            enemy = new Enemy(TileType.ENEMY_FH.getType(), x, y);
            enemy.setMovementStrategy(new FollowMovableElementMovementStrategy(collisionDetectionService, characterProvider));
        }
        else {
            throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        return enemy;
    }

}
