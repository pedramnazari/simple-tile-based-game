package de.pedramnazari.simpletbg.tilemap.service.navigation;

import de.pedramnazari.simpletbg.tilemap.model.IMovableTileElement;
import de.pedramnazari.simpletbg.tilemap.model.Tile;
import de.pedramnazari.simpletbg.tilemap.model.TileMap;

import java.util.Collection;

public class CollisionDetectionService {


    public boolean isCollisionWithObstacle(final TileMap tileMap, IMovableTileElement element) {
        return this.isCollisionWithObstacle(tileMap, element.getX(), element.getY());
    }

    public boolean isCollisionWithObstacle(final TileMap tileMap, final int x, final int y) {
        final Tile newTile = tileMap.getTile(x, y);
        return newTile.isObstacle();
    }

    public boolean isCollision(IMovableTileElement element, Collection<? extends IMovableTileElement> others) {
        return !getCollidingElements(element, others).isEmpty();
    }

    public boolean isCollision(IMovableTileElement element1, IMovableTileElement element2) {
        if (element1 == element2) {
            // Element cannot have a collision with itself
            return false;
        }
        return isCollision(element1.getX(), element1.getY(), element2.getX(), element2.getY());
    }

    private boolean isCollision(int x1, int y1, int x2, int y2) {
        return (x1 == x2) && (y1 == y2);
    }


    public Collection<? extends IMovableTileElement> getCollidingElements(IMovableTileElement element, Collection<? extends IMovableTileElement> others) {
        return others.stream()
                .filter(e -> isCollision(element, e))
                .toList();
    }
}
