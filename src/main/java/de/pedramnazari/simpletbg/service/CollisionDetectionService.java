package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.IMoveableTileElement;
import de.pedramnazari.simpletbg.model.Tile;
import de.pedramnazari.simpletbg.model.TileMap;

import java.util.Collection;

public class CollisionDetectionService {


    public boolean isCollisionWithObstacle(final TileMap tileMap, IMoveableTileElement element) {
        return this.isCollisionWithObstacle(tileMap, element.getX(), element.getY());
    }

    public boolean isCollisionWithObstacle(final TileMap tileMap, final int x, final int y) {
        final Tile newTile = tileMap.getTile(x, y);
        return newTile.isObstacle();
    }

    public boolean isCollision(IMoveableTileElement element, Collection<? extends IMoveableTileElement> others) {
        return !getCollidingElements(element, others).isEmpty();

    }

    public boolean isCollision(IMoveableTileElement element1, IMoveableTileElement element2) {
        return isCollision(element1.getX(), element1.getY(), element2.getX(), element2.getY());
    }

    private boolean isCollision(int x1, int y1, int x2, int y2) {
        return (x1 == x2) && (y1 == y2);
    }


    public Collection<? extends IMoveableTileElement> getCollidingElements(IMoveableTileElement element, Collection<? extends IMoveableTileElement> others) {
        return others.stream()
                .filter(e -> isCollision(element, e))
                .toList();
    }
}
