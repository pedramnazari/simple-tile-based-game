package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.ITileMapElement;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.Collection;

public class CollisionDetectionService {


    public boolean isCollisionWithObstacle(final TileMap tileMap, final int x, final int y) {
        final Tile newTile = tileMap.getTile(x, y);
        return newTile.isObstacle();
    }

    public boolean isCollisionWithObstacleOrOutOfBounds(final TileMap tileMap, final int x, final int y) {
        return !tileMap.isWithinBounds(x, y) || isCollisionWithObstacle(tileMap, x, y);
    }

    public boolean isCollision(ITileMapElement element1, ITileMapElement element2) {
        if (element1 == element2) {
            // Element cannot have a collision with itself
            return false;
        }
        return isCollision(element1.getX(), element1.getY(), element2.getX(), element2.getY());
    }

    private boolean isCollision(int x1, int y1, int x2, int y2) {
        return (x1 == x2) && (y1 == y2);
    }


    public Collection<? extends ITileMapElement> getCollidingElements(ITileMapElement element, Collection<? extends ITileMapElement> others) {
        return others.stream()
                .filter(e -> isCollision(element, e))
                .toList();
    }

}
