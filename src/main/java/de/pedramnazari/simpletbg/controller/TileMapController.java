package de.pedramnazari.simpletbg.controller;

import de.pedramnazari.simpletbg.model.*;
import de.pedramnazari.simpletbg.service.IEnemyObserver;
import de.pedramnazari.simpletbg.service.IItemPickUpListener;
import de.pedramnazari.simpletbg.service.MovementResult;
import de.pedramnazari.simpletbg.service.TileMapService;
import de.pedramnazari.simpletbg.view.TileMapVisualizer;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;

public class TileMapController implements IEnemyObserver, IItemPickUpListener {

    private final TileMapService tileMapService;
    // TODO: remove this dependency
    private TileMapVisualizer tileMapVisualizer;

    public TileMapController(final TileMapService tileMapService) {
        this.tileMapService = tileMapService;
    }

    public void setTileMapVisualizer(TileMapVisualizer tileMapVisualizer) {
        this.tileMapVisualizer = tileMapVisualizer;
    }

    public TileMap startGameUsingMap(Tile[][] tiles, int heroX, int heroY) {
        return tileMapService.createAndInitMap(tiles, List.of(), List.of(), heroX, heroY);
    }

    public void startGameUsingMap(final Tile[][] tiles, Collection<Item> items, Collection<Enemy> enemiesConfig, int heroX, int heroY) {
        tileMapService.createAndInitMap(tiles, items, enemiesConfig, heroX, heroY);
        tileMapService.getHeroMovementService().addItemPickupListener(this);
        tileMapService.getEnemyMovementService().addItemPickupListener(this);

        tileMapService.start();
    }

    public MovementResult moveHeroToRight() {
        return tileMapService.moveHeroToRight();
    }

    public MovementResult moveHeroToLeft() {
        return tileMapService.moveHeroToLeft();
    }

    public MovementResult moveHeroUp() {
        return tileMapService.moveHeroUp();
    }

    public MovementResult moveHeroDown() {
        return tileMapService.moveHeroDown();
    }

    public Collection<Item> getItems() {
        return tileMapService.getItems();
    }


    public TileMap getTileMap() {
        return tileMapService.getTileMap();
    }

    public Hero getHero() {
        return tileMapService.getHero();
    }

    public Collection<Enemy> getEnemies() {
        return tileMapService.getEnemies();
    }

    @Override
    public void update(Collection<Enemy> enemies) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> tileMapVisualizer.updateEnemies(enemies));
    }

    @Override
    public void onItemPickedUp(IItemCollectorElement element, Item item, int itemX, int itemY) {
        // GUI operations must be executed on the JavaFX application thread
        Platform.runLater(() -> tileMapVisualizer.handleItemPickedUp(element, item, itemX, itemY));
    }
}
