package de.pedramnazari.simpletbg.service;

import de.pedramnazari.simpletbg.model.Hero;
import de.pedramnazari.simpletbg.model.ITileFactory;
import de.pedramnazari.simpletbg.model.TileMap;
import de.pedramnazari.simpletbg.model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovementServiceTest {

    private static final int F = TileType.FLOOR.getType();
    private static final int W = TileType.WALL.getType();

    private HeroMovementService movementService;
    private TileMapService tileMapService;
    private Hero hero;

    @BeforeEach
    public void setUp() {
        ITileFactory tileFactory = new DefaultTileFactory(new DefaultItemFactory());

        movementService = new HeroMovementService();

        tileMapService = new TileMapService(tileFactory,
                new DefaultItemFactory(),
                new HeroService(new DefaultHeroFactory(), movementService));
    }

    @Test
    public void testCalcValidMovePositionsWithinMap_AtMapBoundary() {
        final TileMapConfig mapConfig = new TileMapConfig("1", new int[][]{
                {W, F, F},
                {F, W, W},
                {W, F, W}});

        final TileMap tileMap = tileMapService.createAndInitMap(mapConfig, 1, 0);
        assertNotNull(tileMap);

        hero = tileMapService.getHero();
        assertNotNull(hero);

        final Set<Point> validPositions = movementService.calcValidMovePositionsWithinMap(tileMap, hero);
        assertEquals(1, validPositions.size());

        final Point point = validPositions.iterator().next();
        assertEquals(2, point.getX());
        assertEquals(0, point.getY());
    }


    @Test
    public void testCalcValidMovePositionsWithinMap_AtMapEdge() {
        final TileMapConfig mapConfig = new TileMapConfig("1", new int[][]{
                {W, F, F},
                {F, W, W},
                {F, F, W}});

        final TileMap tileMap = tileMapService.createAndInitMap(mapConfig, 0, 2);
        assertNotNull(tileMap);

        hero = tileMapService.getHero();
        assertNotNull(hero);

        final Set<Point> validPositions = movementService.calcValidMovePositionsWithinMap(tileMap, hero);
        assertEquals(2, validPositions.size());

        final Point point1 = validPositions.stream().filter(p -> p.getX() == 0 && p.getY() == 1).findFirst().orElse(null);
        assertNotNull(point1);

        final Point point2 = validPositions.stream().filter(p -> p.getX() == 1 && p.getY() == 2).findFirst().orElse(null);
        assertNotNull(point2);
    }


}
